package com.main.paymentservice.service.impl;

import com.main.paymentservice.exception.PaymentServiceException;
import com.main.paymentservice.models.Transaction;
import com.main.paymentservice.models.TransactionSaga;
import com.main.paymentservice.models.Wallet;
import com.main.paymentservice.models.dto.AmountDTO;
import com.main.paymentservice.models.dto.enums.TransactionStatus;
import com.main.paymentservice.models.dto.request.PaymentRequest;
import com.main.paymentservice.models.dto.response.PaymentResponse;
import com.main.paymentservice.repository.TransactionRepository;
import com.main.paymentservice.service.PaymentService;
import com.main.paymentservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.main.paymentservice.models.dto.enums.TransactionStatus.PENDING_PROCESSING;
import static com.main.paymentservice.models.dto.enums.TransactionStatus.PENDING_QUEUE;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse handlePaymentRequest(PaymentRequest paymentRequest) {
        log.info("Payment request received: {}", paymentRequest);

        try {
            Transaction transaction = getTransaction(paymentRequest);
            transactionRepository.insert(transaction);
            return PaymentResponse.builder().message("Payment request received.").build();
        } catch (Exception e) {
            log.error("Error occurred while processing payment request: {}", paymentRequest, e);
            return PaymentResponse.builder().message("Error occurred while processing payment request.").build();
        }
    }

    @Override
    public List<Transaction> getUnProcessedTransactions() {
        return transactionRepository.findAllByTransactionStatus(PENDING_PROCESSING.getStatus());
    }

    @Override
    public List<Transaction> getProcessingTransactions() {
        return transactionRepository.findAllByTransactionStatus(PENDING_QUEUE.getStatus());
    }

    @Override
    public void updateTransactionStatus(String id, String status) {
        final var transaction = transactionRepository.findById(id);
        transaction.ifPresent(transactionGet -> {
            transactionGet.setTransactionStatus(status);
            transactionRepository.save(transactionGet);
        });
    }

    @Override
    public void updateResult(Transaction transaction, TransactionSaga transactionSaga) {
        TransactionStatus transactionStatus =
                Boolean.TRUE.equals(transactionSaga.getIsSuccessful()) ?
                        TransactionStatus.SUCCESS : TransactionStatus.FAILED;
        transaction.setTransactionStatus(transactionStatus.getStatus());
        transaction.setError(transactionSaga.getError());
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction findByID(String id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new PaymentServiceException("Transaction not found with id: " + id, HttpStatus.SC_NOT_FOUND)
        );
    }

    private Transaction getTransaction(PaymentRequest paymentRequest) {
        return Transaction.builder().cardHolderName(paymentRequest.getCardHolderName())
                .cardNumber(paymentRequest.getCardNo()).cvv(paymentRequest.getCardCvv())
                .amount(AmountDTO.builder().amount(paymentRequest.getAmount()).currency(paymentRequest.getCurrency())
                        .build()).expiryMonth(paymentRequest.getCardExpiryMonth())
                .expiryYear(paymentRequest.getCardExpiryYear()).accountID(paymentRequest.getAccountId())
                .transactionStatus(PENDING_PROCESSING.getStatus()).walletId(getWalletID(paymentRequest.getAccountId()))
                .build();
    }

    private long getWalletID(long accountId) {
        Wallet walletByAccountId = walletService.getWalletByAccountId(accountId);
        return walletByAccountId.getAccountID();
    }
}
