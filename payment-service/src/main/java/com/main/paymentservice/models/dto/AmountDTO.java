package com.main.paymentservice.models.dto;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record AmountDTO(String currency, BigDecimal amount) implements Serializable {
}
