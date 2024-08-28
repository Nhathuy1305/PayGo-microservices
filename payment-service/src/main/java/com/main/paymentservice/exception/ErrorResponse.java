package com.main.paymentservice.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String errorCode,
        String errorMessage
) {
}
