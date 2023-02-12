package com.tapusd.reactivespringrestjwt.domain;

import com.tapusd.reactivespringrestjwt.domain.enums.Roles;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document
public class Account implements Serializable {

    @Id
    @MongoId
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (getUid() != null ? !getUid().equals(account.getUid()) : account.getUid() != null) return false;
        return getEmail() != null ? getEmail().equals(account.getEmail()) : account.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getUid() != null ? getUid().hashCode() : 0;
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
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
