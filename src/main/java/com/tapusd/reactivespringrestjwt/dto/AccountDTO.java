package com.tapusd.reactivespringrestjwt.dto;

import com.tapusd.reactivespringrestjwt.domain.enums.Roles;

import java.util.Set;

public record AccountDTO(String uuid, String email, Set<Roles> roles) {
}
