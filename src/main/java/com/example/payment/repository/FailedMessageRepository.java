package com.example.payment.repository;

import com.example.payment.model.FailedMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedMessageRepository extends MongoRepository<FailedMessage, String> {
}
