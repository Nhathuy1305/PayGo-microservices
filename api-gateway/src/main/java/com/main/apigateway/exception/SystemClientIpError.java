package com.main.apigateway.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class SystemClientIpError extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final String errorMessage;

    public SystemClientIpError(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
