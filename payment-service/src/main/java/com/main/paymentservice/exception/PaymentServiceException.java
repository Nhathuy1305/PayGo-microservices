package com.main.paymentservice.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class PaymentServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String errorMessage;
    private final Integer errorCode;

    public PaymentServiceException(String errorMessage, Integer errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

}
