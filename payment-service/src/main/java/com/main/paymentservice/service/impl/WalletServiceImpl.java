package com.main.paymentservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.paymentservice.models.TransactionSaga;
import com.main.paymentservice.models.Wallet;
import com.main.paymentservice.models.dto.AmountDTO;
import com.main.paymentservice.models.dto.enums.WalletStatus;
import com.main.paymentservice.models.dto.request.UserRegisterEvent;
import com.main.paymentservice.repository.WalletRepository;
import com.main.paymentservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Wallet getWalletByAccountId(long accountId) {
        return walletRepository.findByAccountID(accountId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for account id: " + accountId));
    }

    @Override
    public void handleUserRegister(String userRegisterEvent) {
        try {
            var event = objectMapper.readValue(userRegisterEvent, UserRegisterEvent.class);
            walletRepository.insert(
                    Wallet.builder().accountID(event.accountId()).walletStatus(WalletStatus.ACTIVE.name())
                            .amount(AmountDTO.builder().amount(BigDecimal.ZERO).currency("USD").build()).build()
            );
            log.info("User register event processed: {}", userRegisterEvent);
        } catch (JsonProcessingException e) {
            log.error("Error occurred while processing user register event: {}", userRegisterEvent, e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void chargeAmount(TransactionSaga transactionSaga, AmountDTO amount) {
        Wallet wallet = getWalletByAccountId(Long.parseLong(transactionSaga.getAccountID()));
        BigDecimal newAmount = wallet.getAmount().amount().add(amount.amount());
        wallet.setAmount(AmountDTO.builder().amount(newAmount).currency(amount.currency()).build());
        walletRepository.save(wallet);
        log.info("Amount charged for transaction: {}", transactionSaga);
    }

}
