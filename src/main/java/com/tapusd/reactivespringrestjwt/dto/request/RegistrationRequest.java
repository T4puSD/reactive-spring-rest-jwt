package com.tapusd.reactivespringrestjwt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record RegistrationRequest(@Email(message = "Email must be valid") String email,
                                  @Length(min = 8, max = 32, message = "Minimum 8 character and max 32 character needed")
                                  @Pattern(regexp = "[0-9a-zA-Z!@#$%^&*()]+", message = "Must contain digits small, capital and special characters")
                                  String password) {
}
