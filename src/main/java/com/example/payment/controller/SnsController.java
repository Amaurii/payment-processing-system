package com.example.payment.controller;

import com.example.payment.service.SnsPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sns")
public class SnsController {

    private final SnsPublisher snsPublisher;

    public SnsController(SnsPublisher snsPublisher) {
        this.snsPublisher = snsPublisher;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestBody String message) {
        snsPublisher.publishMessage(message);
        return "Mensagem publicada no SNS!";
    }

}
