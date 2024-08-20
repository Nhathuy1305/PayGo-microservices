package com.main.paymentservice.service;

import com.main.paymentservice.models.TransactionSaga;
import com.main.paymentservice.models.Wallet;
import com.main.paymentservice.models.dto.AmountDTO;

public interface WalletService {

    Wallet getWalletByAccountId(long accountId);

    void handleUserRegister(String userRegisterEvent);

    void chargeAmount(TransactionSaga transactionSaga, AmountDTO amount);

}
