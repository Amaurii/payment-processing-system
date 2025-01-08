package com.example.payment.service;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.model.Payment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;

    public PaymentService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPayment(Payment payment) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_QUEUE, payment);
    }
}
