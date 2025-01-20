package com.example.payment.service;

import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
public class SqsConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SqsConsumer.class);

    @SqsListener("payment-processing")
    public void receiveMessage(String message) {

        logger.info("Mensagem recebida da fila:: {}", message);

        if (message.contains("erro")) {
            throw new RuntimeException("Simulando erro no processamento.");
        }
    }
}
