package com.tapusd.reactivespringrestjwt.web.controller;

import com.tapusd.reactivespringrestjwt.dto.request.AuthRequest;
import com.tapusd.reactivespringrestjwt.dto.response.JWTResponse;
import com.tapusd.reactivespringrestjwt.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<JWTResponse>> authenticate(@Valid AuthRequest authRequest) {
        return authService.authenticate(authRequest)
                .map(ResponseEntity::ok);
    }
}
