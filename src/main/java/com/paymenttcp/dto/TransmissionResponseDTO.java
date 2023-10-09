package com.paymenttcp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class TransmissionResponseDTO {
    private String kernel;
    private String PAN;
    private String amount;
    private String currency;

    @Override
    public String toString() {
        return """
                Kernel: %s
                    Card number: %s
                    Amount: %s
                    Currency: %s
                """.formatted(this.kernel, this.PAN, this.amount, this.currency);
    }
}
