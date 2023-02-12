package com.tapusd.reactivespringrestjwt.repository;

import com.tapusd.reactivespringrestjwt.domain.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, UUID> {

    Mono<Account> findByEmail(String email);
}
