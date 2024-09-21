package com.main.apigateway.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.apigateway.exception.AuthServiceException;
import com.main.apigateway.messaging.producer.UserRegisterProducer;
import com.main.apigateway.models.Account;
import com.main.apigateway.models.Role;
import com.main.apigateway.models.dto.UserRegisterEvent;
import com.main.apigateway.models.dto.request.LoginRequest;
import com.main.apigateway.models.dto.request.SignupRequest;
import com.main.apigateway.models.dto.response.JwtResponse;
import com.main.apigateway.models.dto.response.MessageResponse;
import com.main.apigateway.models.enums.ERole;
import com.main.apigateway.repository.AccountRepository;
import com.main.apigateway.repository.RoleRepository;
import com.main.apigateway.security.jwt.JwtUtils;
import com.main.apigateway.security.services.UserDetailsImpl;
import com.main.apigateway.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRegisterProducer userRegisterProducer;
    private final AccountRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse authUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return JwtResponse.builder().token(jwt).id(userDetails.getId()).username(userDetails.getUsername())
                .email(userDetails.getEmail()).roles(roles).build();
    }

    @Override
    public MessageResponse registerUser(SignupRequest signupRequest) throws JsonProcessingException {
        checkValidations(signupRequest);

        Account user = getAccount(signupRequest);
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        setRoles(strRoles, roles);
        user.setRoles(roles);

        Account save = userRepository.save(user);

        userRegisterProducer.sendUserRegisterEventAsync(UserRegisterEvent.builder().accountId(save.getId()).build());

        return MessageResponse.builder().message("User registered successfully!").build();
    }

    private void checkValidations(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()) ||
                userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AuthServiceException("Error: User is already taken!", HttpStatus.SC_NOT_ACCEPTABLE);
        }
    }

    private Account getAccount(SignupRequest signupRequest) {
        return Account.builder().username(signupRequest.getUsername()).email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword())).fullName(signupRequest.getUsername())
                .phone(signupRequest.getPhoneNumber()).build();
    }

    private void setRoles(Set<String> strRoles, Set<Role> roles) {
        if (Objects.isNull(strRoles) || strRoles.isEmpty()) {
            Role userRole = getUserRole(ERole.ROLE_USER);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role foundRole = switch (role) {
                    case "admin" -> getUserRole(ERole.ROLE_ADMIN);
                    case "mod" -> getUserRole(ERole.ROLE_MODERATOR);
                    default -> getUserRole(ERole.ROLE_USER);
                };
                roles.add(foundRole);
            });
        }
    }

    private Role getUserRole(ERole roleUser) {
        return roleRepository.findByName(roleUser)
                .orElseThrow(() -> new AuthServiceException("Role is not found.", HttpStatus.SC_NOT_FOUND));
    }

}
