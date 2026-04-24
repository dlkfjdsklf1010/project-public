package com.commerceapp.common.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    public String encode(String rawPassword) {
        return BCrypt.withDefaults()
                .hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodePassword) {
        BCrypt.Result result = BCrypt.verifyer()
                .verify(rawPassword.toCharArray(), encodePassword);

        return result.verified;
    }
}
