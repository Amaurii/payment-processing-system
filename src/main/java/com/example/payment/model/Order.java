package com.example.payment.model;


import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "orders")
public class Order implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    private String customerId;
    private BigDecimal totalPrice;
    private String status;
    private List<Product> products;

    public Order() {}

    public Order(String orderId, String customerId, BigDecimal totalPrice, List<Product> products) {
        this.id = orderId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.status = "PROCESSING";
        this.products = products;
    }
}
