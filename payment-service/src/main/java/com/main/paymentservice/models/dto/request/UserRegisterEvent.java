package com.main.paymentservice.models.dto.request;

import lombok.Builder;

@Builder
public record UserRegisterEvent(long accountId) {
}
