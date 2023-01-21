package com.tapusd.reactivespringrestjwt.service;

import com.tapusd.reactivespringrestjwt.domain.Account;
import com.tapusd.reactivespringrestjwt.domain.enums.Roles;
import com.tapusd.reactivespringrestjwt.dto.JWTResponse;
import com.tapusd.reactivespringrestjwt.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl("TestSecretKey", accountRepository, passwordEncoder);
    }

    @Test
    void createJWT_whenCalledWithValidAccount_shouldReturnMonoWithValue() {
        Account account = new Account()
                .setUid(UUID.randomUUID())
                .setRoles(Set.of(Roles.ROLE_ADMIN, Roles.ROLE_USER));

        Mono<JWTResponse> jwt = authService.createJWT(account);

        StepVerifier.create(jwt)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void createJWT_whenCalledWithNullAccount_shouldReturnEmptyMono() {
        Mono<JWTResponse> jwt = authService.createJWT(null);

        StepVerifier.create(jwt)
                .expectNextCount(0)
                .verifyComplete();
    }
}