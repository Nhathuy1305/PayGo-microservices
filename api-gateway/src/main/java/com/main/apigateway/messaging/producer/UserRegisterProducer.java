package com.main.apigateway.messaging.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.apigateway.models.dto.UserRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisterProducer {

    @Value("${kafka.user.topic}")
    private String topicName;
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendUserRegisterEventAsync(UserRegisterEvent userRegisterEvent) throws JsonProcessingException {
        final String key = UUID.randomUUID().toString();
        final String value = objectMapper.writeValueAsString(userRegisterEvent);

        // Send the message and get ListenableFuture
        var future = kafkaTemplate.send(topicName, key, value);

        // Convert ListenableFuture to CompletableFuture
        CompletableFuture<SendResult<String, String>> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return future.get(); // Blocking call
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Handle success and failure
        completableFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                handleFailure(key, value, ex);
            } else {
                handleSuccess(key, value, result);
            }
        });
    }

    private void handleSuccess(String key, String value, SendResult<String, String> result) {
        log.info("Message Sent Successfully for the key {}, the value is {}, partition is {}", key, value,
                result.getRecordMetadata().partition());
    }

    private void handleFailure(String key, String value, Throwable ex) {
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: Key {}, Value {}, Exception {}", key, value, throwable.getMessage());
        }
    }

}
