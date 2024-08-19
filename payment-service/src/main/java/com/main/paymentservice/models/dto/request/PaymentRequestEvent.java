package com.main.paymentservice.models.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.main.paymentservice.models.Transaction;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

@Builder
public record PaymentRequestEvent(
        @JsonProperty("event_id")
        String eventId,
        @JsonProperty("created_date")
        Date createdDate,
        @JsonProperty("transaction")
        Transaction transaction
) implements Serializable {
}
