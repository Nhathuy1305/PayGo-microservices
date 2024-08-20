package com.main.paymentservice.service;

import com.main.paymentservice.models.Transaction;
import com.main.paymentservice.models.TransactionSaga;
import com.main.paymentservice.models.dto.request.PaymentRequest;
import com.main.paymentservice.models.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse handlePaymentRequest(PaymentRequest paymentRequest);

    List<Transaction> getUnProcessedTransactions();

    List<Transaction> getProcessingTransactions();

    void updateTransactionStatus(String id, String status);

    void updateResult(Transaction transaction, TransactionSaga transactionSaga);

    Transaction findByID(String id);

}
