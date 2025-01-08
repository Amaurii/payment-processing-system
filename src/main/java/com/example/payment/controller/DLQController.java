package com.example.payment.controller;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.model.Payment;
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

    // Endpoint para listar mensagens da DLQ
    @GetMapping
    public List<Payment> getAllMessages() {
        List<Payment> messages = new ArrayList<>();
        try {
            while (true) {
                // Recuperar mensagem da DLQ
                var message = rabbitTemplate.receive(RabbitMQConfig.DLQ_QUEUE);
                if (message == null) break;

                // Converter mensagem para o modelo Payment
                Payment payment = (Payment) rabbitTemplate.getMessageConverter().fromMessage(message);
                messages.add(payment);
            }
        } catch (Exception e) {
            logger.error("Erro ao listar mensagens da DLQ", e);
        }
        return messages;
    }

    // Endpoint para reprocessar uma mensagem
    @PostMapping("/reprocess/{id}")
    public String reprocessMessage(@PathVariable String id) {
        try {
            var message = rabbitTemplate.receive(RabbitMQConfig.DLQ_QUEUE);
            if (message == null) {
                return "Mensagem com ID " + id + " não encontrada na DLQ.";
            }

            // Reenviar para a fila principal
            rabbitTemplate.send(RabbitMQConfig.PAYMENT_QUEUE, message);
            return "Mensagem com ID " + id + " reprocessada com sucesso.";
        } catch (Exception e) {
            logger.error("Erro ao reprocessar mensagem", e);
            return "Erro ao reprocessar mensagem com ID " + id;
        }
    }

    // Endpoint para excluir uma mensagem
    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable String id) {
        try {
            var message = rabbitTemplate.receive(RabbitMQConfig.DLQ_QUEUE);
            if (message == null) {
                return "Mensagem com ID " + id + " não encontrada na DLQ.";
            }
            // Excluir a mensagem (simplesmente não reenviaremos)
            logger.info("Mensagem com ID " + id + " excluída da DLQ.");
            return "Mensagem com ID " + id + " excluída com sucesso.";
        } catch (Exception e) {
            logger.error("Erro ao excluir mensagem", e);
            return "Erro ao excluir mensagem com ID " + id;
        }
    }
}
