package com.main.paymentservice.messaging.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.paymentservice.models.dto.request.PaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestProducer {

    @Value("${kafka.payment.topic}")
    private String topicName;
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrderEventAsync(PaymentRequestEvent paymentRequestEvent) throws JsonProcessingException {
        final String key = paymentRequestEvent.eventId();
        final String value = objectMapper.writeValueAsString(paymentRequestEvent);
        kafkaTemplate.setDefaultTopic(topicName);
        final var result = kafkaTemplate.sendDefault(key, value);

        result.thenAccept(sendResult -> handleSuccess(key, value, sendResult))
                .exceptionally(ex -> {
                    handleFailure(key, value, ex);
                    return null;
                });
    }

    private void handleFailure(String key, String value, Throwable ex) {
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: Key {}, Value {}, Exception {}", key, value, throwable.getMessage());
        }
    }

    private void handleSuccess(String key, String value, SendResult<String, String> result) {
        log.info("Message sent successfully for the key {}, value {}, and partition {}", key, value,
                result.getRecordMetadata().partition());
    }

}
