package com.paymenttcp.service.impl;

import com.paymenttcp.dto.TransmissionResponseDTO;
import com.payneteasy.tlv.HexUtil;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void transmissionWithOne_Message_success() {
        String messageHex = "001b02189f2401029f0201015a0841111111111111115f2a02097803";

        val result = transactionService.processTransmission(HexUtil.parseHex(messageHex));
        List<TransmissionResponseDTO> expectedResult = Collections.singletonList(TransmissionResponseDTO.builder().kernel("Mastercard").PAN("4111111111111111").amount("1").currency("EUR").build());

        assertEquals(expectedResult, result);
    }

    @Test
    void transmissionWithValidTransmissionWith2Messages_success() {
        String messageHex = "004002189F2401029F020201005A0841111111111111115F2A0209780302249F240804000000000000005F2A0208269F02031234565A08378282246310005F9F03010003";
        val result = transactionService.processTransmission(HexUtil.parseHex(messageHex));
        List<TransmissionResponseDTO> expectedResult = Arrays.asList(TransmissionResponseDTO.builder().kernel("American Express").PAN("378282246310005").amount("1234.56").currency("GBP").build(), TransmissionResponseDTO.builder().kernel("Mastercard").PAN("4111111111111111").amount("1.00").currency("EUR").build());

        assertEquals(expectedResult, result);
    }

    @Test
    void transmissionWithInvalidMessage_error() {
        String messageHex = "004002189F2401029F020201005A08411111111111103";
        assertThatThrownBy(() -> transactionService.processTransmission(HexUtil.parseHex(messageHex))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void transmissionWithInvalidKernel_ignoreKernel() {
        String messageHex = "001b02189f2401019f020201005a0841111111111111115f2a02097803";

        val result = transactionService.processTransmission(HexUtil.parseHex(messageHex));
        assertTrue(result.isEmpty());
    }

    @Test
    void transmissionWithoutKernel_error() {
        String messageHex = "001b02189f020201005a0841111111111111115f2a02097803";

        assertThatThrownBy(() -> transactionService.processTransmission(HexUtil.parseHex(messageHex))).isInstanceOf(IllegalArgumentException.class);
    }
}
