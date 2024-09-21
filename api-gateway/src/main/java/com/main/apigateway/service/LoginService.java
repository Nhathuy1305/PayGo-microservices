package com.main.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.apigateway.models.dto.request.LoginRequest;
import com.main.apigateway.models.dto.request.SignupRequest;
import com.main.apigateway.models.dto.response.JwtResponse;
import com.main.apigateway.models.dto.response.MessageResponse;

public interface LoginService {

    JwtResponse authUser(LoginRequest loginRequest);

    MessageResponse registerUser(SignupRequest signupRequest) throws JsonProcessingException;

}
