package com.example.payment.service;


import com.example.payment.config.RabbitMQConfig;
import com.example.payment.model.FailedMessage;
import com.example.payment.model.Payment;
import com.example.payment.repository.FailedMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class DLQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DLQConsumer.class);

    private final FailedMessageRepository failedMessageRepository;


    private final RabbitTemplate rabbitTemplate;

    public DLQConsumer(FailedMessageRepository failedMessageRepository, RabbitTemplate rabbitTemplate) {
        this.failedMessageRepository = failedMessageRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "dlq.queue")
    public void processFailedMessages(Payment payment) {
        logger.warn("Received message in DLQ: {}", payment.getCustomerId());

        try {
            logger.info("Reprocessing message: {}", payment);
            rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_QUEUE, payment);

            FailedMessage failedMessage = new FailedMessage(
                    payment.toString(),
                    "Validation failed or processing error"
            );
            failedMessageRepository.save(failedMessage);
            logger.info("Failed message saved to MongoDB");
        } catch (Exception e) {
            logger.error("Failed to reprocess message: {}", payment, e);
        }
    }
}
