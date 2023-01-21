package com.tapusd.reactivespringrestjwt.service;

import com.tapusd.reactivespringrestjwt.domain.Account;
import com.tapusd.reactivespringrestjwt.domain.enums.Roles;
import com.tapusd.reactivespringrestjwt.dto.JWTResponse;
import com.tapusd.reactivespringrestjwt.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
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

    @Test
    void verifyJWT_whenCalledWithNullJWT_shouldReturnEmptyMono() {
        Mono<Account> accountMono = authService.verifyJWT(null);

        StepVerifier.create(accountMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void verifyJWT_whenCalledWithInvalidJWT_shouldReturnEmptyMono() {
        Mono<Account> accountMono = authService.verifyJWT("invalidjwtstring");

        StepVerifier.create(accountMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void verifyJWT_whenCalledWithValidJWT_shouldReturnAccount() {
        UUID uuid = UUID.randomUUID();
        Account account = new Account()
                .setUid(uuid)
                .setRoles(Set.of(Roles.ROLE_ADMIN, Roles.ROLE_USER));

        Optional<JWTResponse> jwtResponse = authService.createJWT(account)
                .blockOptional();

        assertThat(jwtResponse)
                .isNotEmpty()
                .get()
                .extracting("jwt")
                .isNotNull();

        Mockito.when(accountRepository.findById(uuid))
                .thenReturn(Mono.just(account));
        Mono<Account> accountMono = authService.verifyJWT(jwtResponse.get().jwt());

        StepVerifier.create(accountMono)
                .expectNextMatches(account1 -> account1.getUid().equals(uuid))
                .verifyComplete();
    }
}