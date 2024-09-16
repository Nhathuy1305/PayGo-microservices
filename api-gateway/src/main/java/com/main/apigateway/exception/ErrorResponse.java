package com.main.apigateway.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String errorCode,
        String errorMessage
) {
}
