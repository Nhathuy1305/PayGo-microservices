package com.main.apigateway.models.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
public final class PaymentRequest {

    private BigDecimal amount;
    private String currency;
    private String cardNo;
    private int cardExpiryMonth;
    private int cardExpiryYear;
    private String cardHolderName;
    private String cardCvv;
    private long accountId;
    private String mail;

}
