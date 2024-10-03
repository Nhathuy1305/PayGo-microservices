package com.main.apigateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.apigateway.models.dto.request.LoginRequest;
import com.main.apigateway.models.dto.request.SignupRequest;
import com.main.apigateway.models.dto.response.JwtResponse;
import com.main.apigateway.models.dto.response.MessageResponse;
import com.main.apigateway.service.LoginService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/signing")
    @CircuitBreaker(name = "beCommon")
    @RateLimiter(name = "beCommon")
    @Retry(name = "beCommon")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        final var loginResp = loginService.authUser(loginRequest);
        return ResponseEntity.ok(loginResp);
    }

    @PostMapping("/signup")
    @CircuitBreaker(name = "beCommon")
    @RateLimiter(name = "beCommon")
    @Retry(name = "beCommon")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest)
        throws JsonProcessingException {
        final var registerResp = loginService.registerUser(signupRequest);
        return ResponseEntity.ok(registerResp);
    }

}
