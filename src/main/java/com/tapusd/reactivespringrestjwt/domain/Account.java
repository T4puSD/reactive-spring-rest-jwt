package com.tapusd.reactivespringrestjwt.domain;

import com.tapusd.reactivespringrestjwt.domain.enums.Roles;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RedisHash(value = "account")
public class Account implements Serializable {

    private UUID uid;
    private String email;
    private String password;

    private Set<Roles> roles;

    public UUID getUid() {
        return uid;
    }

    public Account setUid(UUID uid) {
        this.uid = uid;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Account setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public Account setRoles(Set<Roles> roles) {
        this.roles = roles;
        return this;
    }

    public List<String> getRoleNames() {
        return getRoles().stream()
                .map(Enum::name)
                .toList();
    }

    @Override
    public String toString() {
        return "Account{" +
                "uid=" + uid +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
