package com.tapusd.reactivespringrestjwt.controller.v1;

import com.tapusd.reactivespringrestjwt.dto.AuthRequest;
import com.tapusd.reactivespringrestjwt.dto.JWTResponse;
import com.tapusd.reactivespringrestjwt.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
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
