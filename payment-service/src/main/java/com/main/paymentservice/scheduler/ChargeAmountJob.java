package com.main.paymentservice.scheduler;

import com.main.paymentservice.models.Transaction;
import com.main.paymentservice.models.TransactionSaga;
import com.main.paymentservice.models.dto.AmountDTO;
import com.main.paymentservice.models.dto.enums.SagaStatus;
import com.main.paymentservice.service.PaymentService;
import com.main.paymentservice.service.SagaCouchbaseRepository;
import com.main.paymentservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChargeAmountJob {

    private final SagaCouchbaseRepository sagaCouchbaseRepository;
    private final PaymentService paymentService;
    private final WalletService walletService;

    @Scheduled(cron = "0/5 0/1 * 1/1 * *")
    public void chargeAmount() {
        log.info("ChargeAmountJob started");
        sagaCouchbaseRepository.findAlreadySendNotifyRecords().forEach(transactionSaga -> {
            if (Boolean.TRUE.equals(transactionSaga.getIsSuccessful())) {
                AmountDTO amount = getAmountByTransactionID(transactionSaga.getTransactionID());
                walletService.chargeAmount(transactionSaga, amount);
                updateSagaRecord(transactionSaga);
            }
        });
        log.info("ChargeAmountJob finished");
    }

    private AmountDTO getAmountByTransactionID(String transactionID) {
        Transaction transaction = paymentService.findByID(transactionID);
        return transaction.getAmount();
    }

    private void updateSagaRecord(TransactionSaga transactionSaga) {
        transactionSaga.setSagaStatus(SagaStatus.TRANSACTION_DONE.name());
        sagaCouchbaseRepository.update(transactionSaga.getSagaId(), transactionSaga);
    }

}
