package com.main.apigateway.controller;

import com.main.apigateway.client.PaymentClient;
import com.main.apigateway.models.dto.request.PaymentRequest;
import com.main.apigateway.models.dto.response.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    @Qualifier("com.main.apigateway.client.PaymentClient")
    private final PaymentClient paymentClient;

    @PostMapping
    @CircuitBreaker(name = "beCommon")
    @RateLimiter(name = "beCommon")
    @Retry(name = "beCommon")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> payment(@RequestBody PaymentRequest paymentRequest) {
        final var paymentResp = paymentClient.handlePaymentRequest(paymentRequest);
        return ResponseEntity.ok(paymentResp.getBody());
    }

}
