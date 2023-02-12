package com.tapusd.reactivespringrestjwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Payload;
import com.tapusd.reactivespringrestjwt.domain.Account;
import com.tapusd.reactivespringrestjwt.domain.enums.Roles;
import com.tapusd.reactivespringrestjwt.dto.request.AuthRequest;
import com.tapusd.reactivespringrestjwt.dto.request.RegistrationRequest;
import com.tapusd.reactivespringrestjwt.dto.response.JWTResponse;
import com.tapusd.reactivespringrestjwt.exception.NotFoundException;
import com.tapusd.reactivespringrestjwt.repository.AccountRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String ISSUER = "reactive-spring-rest-jwt";
    private static final String ROLES_KEY = "roles";
    private static final long TOKEN_VALIDITY_IN_MILLISECONDS = 60 * 60000L; // 1 hour

    private final Algorithm algorithm;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(@Value("${app.jwt.hmac.secret-key}") String hmacSecretKey,
                           AccountService accountService,
                           PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        algorithm = Algorithm.HMAC256(hmacSecretKey);
    }

    @Override
    public Mono<JWTResponse> authenticate(AuthRequest authRequest) {
        Assert.notNull(authRequest, "Auth request can not be null!");
        Assert.notNull(authRequest.email(), "Username can not be null!");
        Assert.notNull(authRequest.password(), "Password can not be null!");
        return accountService.findByEmail(authRequest.email())
                .filter(account -> passwordEncoder.matches(authRequest.password(), account.getPassword()))
                .flatMap(this::createJWT);
    }

    @Override
    public Mono<JWTResponse> createJWT(Account account) {
        return Mono.justOrEmpty(account)
                .map(account1 -> JWT.create()
                        .withIssuer(ISSUER)
                        .withClaim(ROLES_KEY, account1.getRoleNames())
                        .withSubject(account1.getUid().toString())
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(new Date().getTime() + TOKEN_VALIDITY_IN_MILLISECONDS))
                        .sign(algorithm))
                .map(JWTResponse::new)
                .onErrorResume(JWTCreationException.class::isInstance, throwable -> {
                    LOGGER.error("Unable to create JWT token for user: {}", account.getUid(), throwable);
                    return Mono.empty();
                });

    }

    @Override
    public Mono<Account> verifyJWT(String jwt) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();

        return Mono.justOrEmpty(jwt)
                .flatMap(jwtToken -> Mono.justOrEmpty(verifier.verify(jwtToken))
                        .map(Payload::getSubject)
                        .map(ObjectId::new)
                        .flatMap(accountService::findById))
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                .onErrorResume(JWTVerificationException.class::isInstance, throwable -> {
                    LOGGER.error("Invalid jwt token", throwable);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Account> registerUser(RegistrationRequest request) {
        var account = new Account()
                .setEmail(request.email())
                .setPassword(passwordEncoder.encode(request.password()))
                .setRoles(Collections.singleton(Roles.ROLE_USER));

        return accountService.save(account);
    }
}
