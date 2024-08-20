package com.main.paymentservice.service;

import com.main.paymentservice.models.Transaction;
import com.main.paymentservice.models.TransactionSaga;

import java.util.List;

public interface SagaCouchbaseRepository {

    void createSagaRecord(Transaction paymentRequestEvent);

    List<TransactionSaga> findByTransactionID(String transactionID);

    void update(String sagaId, TransactionSaga transactionSaga);

    List<TransactionSaga> findAlreadySendNotifyRecords();

}
