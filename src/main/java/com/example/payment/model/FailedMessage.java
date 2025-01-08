package com.example.payment.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "failed_messages")
public class FailedMessage {

    @Id
    private String id;
    private String messageContent;
    private String errorDetails;
    private LocalDateTime timestamp;

    public FailedMessage() {}

    public FailedMessage(String messageContent, String errorDetails) {
        this.messageContent = messageContent;
        this.errorDetails = errorDetails;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
