package com.paymenttcp.mappers;

import com.paymenttcp.dto.TransmissionResponseDTO;
import com.paymenttcp.enums.KernelProvidersEnum;
import com.paymenttcp.enums.TagsEnum;
import com.payneteasy.tlv.BerTlvs;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.paymenttcp.utils.Utils.extractTagValue;

public interface TransmissionMapper {

    static List<TransmissionResponseDTO> mapToTransmissionResponseDTO(List<BerTlvs> listTLV) {
        return listTLV.stream()
                .map(message ->
                        TransmissionResponseDTO.builder()
                                .kernel(buildKernel(message))
                                .PAN(buildPAN(message))
                                .amount(buildAmount(message))
                                .currency(buildCurrency(message))
                                .build())
                .sorted(Comparator.comparing(TransmissionResponseDTO::getKernel))
                .toList();
    }

    static String buildKernel(BerTlvs berTlvs) {
        return KernelProvidersEnum.searchKernelById(extractTagValue(berTlvs, TagsEnum.KERNEL).replaceAll("0", "")).getKernelProviderName();
    }

    static String buildPAN(BerTlvs berTlvs) {
        String tagHex = extractTagValue(berTlvs, TagsEnum.PAN);

        if (Character.isLetter(tagHex.charAt(tagHex.length() - 1))) {
            return tagHex.substring(0, tagHex.length() - 1);
        }
        return tagHex;
    }

    static String buildAmount(BerTlvs berTlvs) {
        String tagHex = extractTagValue(berTlvs, TagsEnum.AMOUNT);

        String amount = tagHex.replaceAll("^0+", "");

        if (amount.length() > 2) {
            String prefix = amount.substring(0, amount.length() - 2);
            String lastTwoChars = amount.substring(amount.length() - 2);
            return prefix + "." + lastTwoChars;
        } else {
            return amount;
        }
    }

    static String buildCurrency(BerTlvs berTlvs) {
        String tagHex = extractTagValue(berTlvs, TagsEnum.CURRENCY);

        Collection<CurrencyUnit> currencies = Monetary.getCurrencies();

        CurrencyUnit currency = currencies.stream()
                .filter(currencyUnit -> currencyUnit.getNumericCode() == Integer.parseInt(tagHex))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Currency not found for code: " + tagHex));

        return currency.getCurrencyCode();
    }
}
