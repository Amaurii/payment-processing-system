package com.example.payment.service;

import com.example.payment.model.Order;
import com.example.payment.repository.OrderRepository;
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
import java.util.Optional;
import java.util.Set;

import static com.example.payment.config.RabbitMQConfig.ORDER_QUEUE;

@Service
@Validated
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
    private final Validator validator;
    private final OrderRepository orderRepository;
    private final OrderCacheService orderCacheService;
    private final io.micrometer.core.instrument.Counter successCounter;

    public OrderConsumer(Validator validator, OrderRepository paymentRepository, OrderCacheService orderCacheService, MeterRegistry meterRegistry) {
        this.validator = validator;
        this.orderRepository = paymentRepository;
        this.orderCacheService = orderCacheService;
        this.successCounter = meterRegistry.counter("rabbitmq.messages.processed.success");
    }

    @RabbitListener(queues = ORDER_QUEUE)
    public void consumePayment(Order order, Channel channel, Message message) {
        try {
            Set<ConstraintViolation<Order>> violations = validator.validate(order);
            if (!violations.isEmpty()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors:");
                violations.forEach(v -> errorMessages.append(" ").append(v.getMessage()));
                logger.error("Invalid order detected: {}", errorMessages.toString());
                throw new IllegalArgumentException(errorMessages.toString());
            }

            if (orderCacheService.isOrderProcessed(order.getId())) {
                System.out.println("Pedido duplicado detectado no Redis! Ignorando: " + order.getId());
                return;
            }

            Optional<Order> existingOrder = orderRepository.findById(order.getId());
            if (existingOrder.isPresent()) {
                System.out.println("Pedido duplicado detectado! Ignorando: " + order.getCustomerId());
                return;
            }

            orderCacheService.markOrderAsProcessed(order.getId());

            orderRepository.save(order);
            logger.info("Order saved: {}", order.getId() + " " + order.getCustomerId() + " " + order.getStatus());
            successCounter.increment();

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info("Order processed successfully: {}", order);

        }catch (Exception e){
                logger.error("Error processing order: {}", order, e);
            try {
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ioException) {
                logger.error("Failed to reject message", ioException);
            }
        }
    }
}
