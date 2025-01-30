package com.example.payment.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OrderCacheService {

    private final StringRedisTemplate redisTemplate;

    public OrderCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isOrderProcessed(String orderId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(orderId));
    }

    public void markOrderAsProcessed(String orderId) {
        redisTemplate.opsForValue().set(orderId, "processed", 1, TimeUnit.MINUTES);
    }

}
