package com.main.apigateway.models.dto;

import lombok.Builder;

@Builder
public record UserRegisterEvent(long accountId) {
}
