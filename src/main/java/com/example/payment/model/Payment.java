package com.example.payment.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public class Payment implements Serializable {

    @NotNull(message = "Payment ID cannot be null")
    private String paymentId;

    @NotNull(message = "Currency cannot be null")
    private String customerId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than zero")
    private double amount;

    public Payment() {}

    public Payment(String paymentId, String customerId, double amount) {
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toString() {
        return super.toString();
    }
}
