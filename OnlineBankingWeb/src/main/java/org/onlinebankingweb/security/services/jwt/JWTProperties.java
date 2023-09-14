package org.onlinebankingweb.security.services.jwt;

import java.time.Duration;

public class JWTProperties {
    private final String secretKey;
    private final Duration tokenDuration;

    public JWTProperties(String secretKey, Duration tokenDuration) {
        this.secretKey = secretKey;
        this.tokenDuration = tokenDuration;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public Duration getTokenDuration() {
        return tokenDuration;
    }
}
