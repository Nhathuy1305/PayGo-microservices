package com.main.apigateway.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class AuthServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer errorCode;
    private final String errorMessage;

    public AuthServiceException(String errorMessage, Integer errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
