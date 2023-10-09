package com.paymenttcp.service;

import com.paymenttcp.dto.TransmissionResponseDTO;

import java.util.List;

public interface TransactionService {

    List<TransmissionResponseDTO> processTransmission(byte[] transmission);
}
