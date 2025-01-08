package com.example.payment.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;

@Service
public class DLQMetricsService {

    private final Counter dlqMessagesCounter;

    public DLQMetricsService(MeterRegistry meterRegistry) {
        this.dlqMessagesCounter = meterRegistry.counter("dlq.messages.count");
    }

    public void incrementDLQCounter() {
        dlqMessagesCounter.increment();
    }


}
