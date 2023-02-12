package com.tapusd.reactivespringrestjwt.web.controller.user;

import com.tapusd.reactivespringrestjwt.dto.AccountDTO;
import com.tapusd.reactivespringrestjwt.util.AuthContextUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/my")
public class MyAccountController {

    @GetMapping("/info")
    public Mono<ResponseEntity<AccountDTO>> getMyInfo() {
        return AuthContextUtils.getAccountFromAuthContext()
                .map(account -> ResponseEntity.ok(new AccountDTO(account.getUid().toHexString(),
                        account.getEmail(), account.getRoles())));
    }
}
