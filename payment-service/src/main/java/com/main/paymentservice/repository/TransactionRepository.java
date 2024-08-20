package com.main.paymentservice.repository;

import com.main.paymentservice.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findAllByTransactionStatus(String status);

}
