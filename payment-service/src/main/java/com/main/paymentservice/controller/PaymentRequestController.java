package com.main.paymentservice.controller;

import com.main.paymentservice.models.dto.request.PaymentRequest;
import com.main.paymentservice.models.dto.response.PaymentResponse;
import com.main.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentRequestController {

    private final PaymentService paymentService;
    @PostMapping
    public ResponseEntity<PaymentResponse> handlePaymentRequest(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.handlePaymentRequest(paymentRequest));
    }

}
