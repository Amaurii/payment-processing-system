package com.example.payment.service;

import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import com.rabbitmq.client.Channel;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.util.Set;

import static com.example.payment.config.RabbitMQConfig.PAYMENT_QUEUE;

@Service
@Validated
public class PaymentConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);
    private final Validator validator;
    private final PaymentRepository paymentRepository;
    private final MeterRegistry meterRegistry;
    private final io.micrometer.core.instrument.Counter successCounter;

    public PaymentConsumer(Validator validator, PaymentRepository paymentRepository, MeterRegistry meterRegistry) {
        this.validator = validator;
        this.paymentRepository = paymentRepository;
        this.meterRegistry = meterRegistry;

        this.successCounter = meterRegistry.counter("rabbitmq.messages.processed.success");

    }

    @RabbitListener(queues = PAYMENT_QUEUE)
    public void consumePayment(Payment payment, Channel channel, Message message) {
        try {
            Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
            if (!violations.isEmpty()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors:");
                violations.forEach(v -> errorMessages.append(" ").append(v.getMessage()));

                logger.error("Invalid payment detected: {}", errorMessages.toString());

                throw new IllegalArgumentException(errorMessages.toString());
            }

            paymentRepository.save(payment);
            logger.info("Payment saved: {}", payment.getPaymentId() + " " + payment.getCustomerId() + " " + payment.getAmount());
            successCounter.increment();

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info("Payment processed successfully: {}", payment);

        }catch (Exception e){
            logger.error("Error processing payment: {}", payment, e);

            try {
                // Rejeitar mensagem e enviá-la para a DLQ
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ioException) {
                logger.error("Failed to reject message", ioException);
            }
        }
    }
}
