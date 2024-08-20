package com.main.paymentservice.repository;

import com.main.paymentservice.models.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet, String> {

    Optional<Wallet> findByAccountID(long accountId);

}