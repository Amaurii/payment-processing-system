package com.example.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;

@Service
public class SnsPublisher {

    private final NotificationMessagingTemplate snsTemplate;

    @Value("${cloud.aws.sns.topic.arn}")
    private String snsTopicArn;

    public SnsPublisher(NotificationMessagingTemplate snsTemplate) {
        this.snsTemplate = snsTemplate;
    }

    public void publishMessage(String message) {
        snsTemplate.sendNotification(snsTopicArn, message, "SNS Notification");
    }

}
