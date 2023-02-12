package com.tapusd.reactivespringrestjwt.config;

import com.tapusd.reactivespringrestjwt.repository.AccountRepository;
import com.tapusd.reactivespringrestjwt.service.AuthService;
import com.tapusd.reactivespringrestjwt.web.filters.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final AccountRepository accountRepository;

    @Autowired
    public SecurityConfig(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> accountRepository.findByEmail(username)
                .map(account -> new User(account.getEmail(), account.getPassword(), account.getGrantedAuthorities()));
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, AuthService authService) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(httpBasicSpec ->
                        // to disable http basic login popup at browser end when the jwt is expired
                        httpBasicSpec.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .authorizeExchange(exchange -> exchange.pathMatchers("/api/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/auth/**").permitAll()
                        .anyExchange().hasRole("USER"))
                .addFilterAt(new JWTAuthenticationFilter(authService), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }
}
