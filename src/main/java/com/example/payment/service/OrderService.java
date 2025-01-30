package com.example.payment.service;

import com.example.payment.config.RabbitMQConfig;
import com.example.payment.dto.OrderRequest;
import com.example.payment.model.Order;
import com.example.payment.model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final RabbitTemplate rabbitTemplate;

    public OrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(OrderRequest orderRequest) {
        Order order = new Order(UUID.randomUUID().toString(), orderRequest.getCustomerId(), calculateTotal(orderRequest.getProducts()), orderRequest.getProducts());
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, order);
    }

    private BigDecimal calculateTotal(List<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
