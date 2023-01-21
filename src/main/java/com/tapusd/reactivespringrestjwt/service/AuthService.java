package com.tapusd.reactivespringrestjwt.service;

import com.tapusd.reactivespringrestjwt.domain.Account;
import com.tapusd.reactivespringrestjwt.dto.AuthRequest;
import com.tapusd.reactivespringrestjwt.dto.JWTResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<JWTResponse> authenticate(AuthRequest authRequest);

    Mono<JWTResponse> createJWT(Account account);

    Mono<Account> verifyJWT(String jwt);
}
