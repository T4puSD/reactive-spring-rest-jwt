package com.tapusd.reactivespringrestjwt.web.controller.admin;

import com.tapusd.reactivespringrestjwt.domain.Account;
import com.tapusd.reactivespringrestjwt.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/admin/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Flux<Account> getAllUsers() {
        return accountService.findAll();
    }
}
