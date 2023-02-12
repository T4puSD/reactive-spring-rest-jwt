package com.tapusd.reactivespringrestjwt.service;

import com.tapusd.reactivespringrestjwt.domain.Account;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<Account> save(Account account);

    Mono<Account> findById(ObjectId objectId);

    Mono<Account> findByEmail(String email);
}
