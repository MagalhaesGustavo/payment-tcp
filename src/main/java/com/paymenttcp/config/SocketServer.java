package com.paymenttcp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayRawSerializer;
import org.springframework.messaging.MessageChannel;

@EnableIntegration
@Configuration
public class SocketServer {

    @Value("${socket.server-port}")
    private int port;

    @Bean
    public AbstractServerConnectionFactory serverFactory() {
        TcpNetServerConnectionFactory tcpServerFactory = new TcpNetServerConnectionFactory(port);
        ByteArrayRawSerializer byteArrayRawSerializer = new ByteArrayRawSerializer();
        byteArrayRawSerializer.setMaxMessageSize(65535);
        tcpServerFactory.setDeserializer(byteArrayRawSerializer);
        return tcpServerFactory;
    }

    @Bean
    public MessageChannel tcpChannel() {
        return new DirectChannel();
    }

    @Bean
    public TcpInboundGateway tcpInGate(AbstractServerConnectionFactory connectionFactory) {
        TcpInboundGateway inGateway = new TcpInboundGateway();
        inGateway.setConnectionFactory(connectionFactory);
        inGateway.setRequestChannel(tcpChannel());
        return inGateway;
    }
}