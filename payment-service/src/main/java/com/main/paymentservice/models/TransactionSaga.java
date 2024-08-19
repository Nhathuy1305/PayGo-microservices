package com.main.paymentservice.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSaga {

    private String sagaId;

    private String transactionID;

    private String accountID;

    private String sagaStatus;

    private String error;

    private Boolean isSuccessful;

}
