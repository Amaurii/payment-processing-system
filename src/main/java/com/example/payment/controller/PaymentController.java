package com.example.payment.controller;

import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody Payment payment) {
        payment.setPaymentId(UUID.randomUUID().toString());
        paymentService.sendPayment(payment);
        return ResponseEntity.ok("Payment queued successfully with ID: " + payment.getPaymentId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String id) {
        return paymentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
