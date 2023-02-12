package com.tapusd.reactivespringrestjwt.web.controller.user;

import com.tapusd.reactivespringrestjwt.domain.Account;
import com.tapusd.reactivespringrestjwt.dto.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/my")
public class MyAccountController {

    @GetMapping("/info")
    public Mono<ResponseEntity<AccountDTO>> getMyInfo() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    var authentication = context.getAuthentication();
                    var account = (Account) authentication.getPrincipal();
                    if (Objects.isNull(account)) {
                        throw new IllegalStateException("Not authenticated");
                    }

                    return ResponseEntity.ok(new AccountDTO(account.getUid().toHexString(),
                            account.getEmail(), account.getRoles()));
                });
    }
}
