package com.example.payment.controller;

import com.example.payment.dto.OrderRequest;
import com.example.payment.model.Order;
import com.example.payment.repository.OrderRepository;
import com.example.payment.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository paymentRepository) {
        this.orderService = orderService;
        this.orderRepository = paymentRepository;
    }

    @GetMapping
    public List<Order> getAllPayments() {
        return orderRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.sendOrder(orderRequest);
        return ResponseEntity.ok("Order queued successfully with ID: " + orderRequest.getCustomerId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
