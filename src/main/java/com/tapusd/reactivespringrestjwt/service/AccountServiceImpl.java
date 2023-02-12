package com.tapusd.reactivespringrestjwt.service;

import com.tapusd.reactivespringrestjwt.domain.Account;
import com.tapusd.reactivespringrestjwt.repository.AccountRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements com.tapusd.reactivespringrestjwt.service.AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<Account> save(Account account) {
        return accountRepository.save(account)
                .onErrorResume(DuplicateKeyException.class::isInstance,
                        (Throwable throwable) -> Mono.error(new IllegalArgumentException("Email already taken")));
    }

    @Override
    public Mono<Account> findById(ObjectId objectId) {
        return accountRepository.findById(objectId);
    }

    @Override
    public Mono<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
