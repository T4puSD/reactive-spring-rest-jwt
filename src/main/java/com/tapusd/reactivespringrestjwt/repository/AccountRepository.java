package com.tapusd.reactivespringrestjwt.repository;

import com.tapusd.reactivespringrestjwt.domain.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, ObjectId> {

    Mono<Account> findByEmail(String email);
}
