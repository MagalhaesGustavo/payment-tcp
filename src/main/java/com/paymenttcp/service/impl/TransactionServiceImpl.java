package com.paymenttcp.service.impl;

import com.paymenttcp.dto.TransmissionResponseDTO;
import com.paymenttcp.enums.TagsEnum;
import com.paymenttcp.service.TransactionService;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;
import com.payneteasy.tlv.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.paymenttcp.enums.KernelProvidersEnum.isKernelProviderValid;
import static com.paymenttcp.mappers.TransmissionMapper.mapToTransmissionResponseDTO;
import static com.paymenttcp.utils.Utils.bytesToHex;
import static com.paymenttcp.utils.Utils.extractTagValue;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    public static final String STX = "02";
    public static final String ETX = "03";
    public static final int LENGTH_TRANSMISSION = 4;
    public static final int LENGTH_DATA = 2;

    public List<TransmissionResponseDTO> processTransmission(byte[] transmissionBytes) {
        String transmissionList = bytesToHex(transmissionBytes);

        List<String> messagesList = getMessageList(transmissionList);

        List<BerTlvs> messagesTLV = convertMessagesToTLV(messagesList);

        removeInvalidKernels(messagesTLV);

        return mapToTransmissionResponseDTO(messagesTLV);
    }

    private List<BerTlvs> convertMessagesToTLV(List<String> messagesList) {
        log.info("Converting messages to TLV structure");
        List<BerTlvs> convertedMessageList = new ArrayList<>();

        messagesList.forEach(message -> {
            try {
                byte[] bytes = HexUtil.parseHex(message);
                BerTlvParser parser = new BerTlvParser();
                convertedMessageList.add(parser.parse(bytes, 0, bytes.length));
            } catch (Exception e) {
                log.error("Error while parsing the message to TLV structure. Error: " + e.getMessage());
                throw new IllegalArgumentException("Error to convert message to TLV. Message: " + message);
            }
        });

        return convertedMessageList;
    }

    private List<String> getMessageList(String transmission) {
        String transmissionWithoutSizeInfo = transmission.substring(LENGTH_TRANSMISSION);

        return Arrays.stream(transmissionWithoutSizeInfo.split(ETX + STX))
                .map(string -> string.startsWith(STX) ? string.substring(2) : string)
                .map(s -> s.substring(LENGTH_DATA))
                .map(s -> s.endsWith(ETX) ? s.substring(0, s.length() - 2) : s)
                .toList();
    }

    private void removeInvalidKernels(List<BerTlvs> messagesTLV) {
        messagesTLV.removeIf(this::isInvalidKernel);
    }

    private boolean isInvalidKernel(BerTlvs berTlvs) {
        String tagHex = extractTagValue(berTlvs, TagsEnum.KERNEL);
        String kernelValue = tagHex.replaceAll("0", "");

        return !isKernelProviderValid(kernelValue);
    }
}
