package com.coremarker.demo_login.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record RegisterUserDTO(String username,
                              String email,
                              @JsonIgnore String password,
                              String fullName) {
    @Override
    public String toString() {
        return "[username = " + username + ", email = " + email + ", fullName = " + fullName + "]";
    }
}
