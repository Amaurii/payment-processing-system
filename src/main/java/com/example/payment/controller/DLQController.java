package com.example.payment.controller;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dlq/messages")
public class DLQController {

    private static final Logger logger = LoggerFactory.getLogger(DLQController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public List<Order> getAllMessages() {
        List<Order> messages = new ArrayList<>();
        try {
            while (true) {
                var message = rabbitTemplate.receive(RabbitMQConfig.DLQ_QUEUE);
                if (message == null) break;

                Order payment = (Order) rabbitTemplate.getMessageConverter().fromMessage(message);
                messages.add(payment);
            }
        } catch (Exception e) {
            logger.error("Erro ao listar mensagens da DLQ", e);
        }
        return messages;
    }
}
