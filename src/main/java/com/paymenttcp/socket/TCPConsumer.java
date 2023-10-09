package com.paymenttcp.socket;

import com.paymenttcp.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

@Slf4j
@MessageEndpoint
public class TCPConsumer {

    @Autowired
    private TransactionService accountService;

    @Transformer(inputChannel = "tcpChannel")
    public String consume(byte[] bytes) {
        try {
            log.info("Processing transmission");
            return accountService.processTransmission(bytes).toString();
        } catch (Exception exception) {
            log.error("Error: " + exception.getLocalizedMessage());
            return "An error occurred while processing the transmission, check the application log for more information";
        }
    }
}
