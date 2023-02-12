package com.tapusd.reactivespringrestjwt.util;

import com.tapusd.reactivespringrestjwt.domain.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

public class AuthContextUtils {

    private AuthContextUtils() {
    }

    public static Mono<Account> getAccountFromAuthContext() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    if (!authentication.isAuthenticated()) {
                        throw new IllegalStateException("Not authenticated");
                    }

                    return (Account) authentication.getPrincipal();
                });
    }
}
