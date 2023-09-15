package org.onlinebankingweb.security.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.onlinebanking.core.domain.models.user.UserRole;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class JWTService {
    private final JWTProperties properties;

    public JWTService(JWTProperties properties) {
        this.properties = properties;
    }

    public String issueToken(Request request) {

        return JWT.create()
                .withSubject(String.valueOf(request.userId))
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(properties.getTokenDuration()))
                .withClaim("e", request.getEmail())
                .withClaim("au", request.getRoles().stream().map(UserRole::toString).toList())
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
                .build()
                .verify(token);
    }

    public UserPrincipal convertToPrincipal(DecodedJWT jwt) {
        List<SimpleGrantedAuthority> authorityList = getClaimOrEmptyList(jwt, "au").stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUserId(Long.parseLong(jwt.getSubject()));
        userPrincipal.setEmail(jwt.getClaim("e").asString());
        userPrincipal.setAuthorities(authorityList);

        return userPrincipal;
    }

    private List<String> getClaimOrEmptyList(DecodedJWT jwt, String claim) {
        if (jwt.getClaim(claim).isNull()) {
            return List.of();
        }
        return jwt.getClaim(claim).asList(String.class);
    }

    public static final class Request {
        private final Long userId;
        private final String email;
        private final List<UserRole> roles;

        public Request(Long userId, String email, List<UserRole> roles) {
            this.userId = userId;
            this.email = email;
            this.roles = roles;
        }

        public Long getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public List<UserRole> getRoles() {
            return roles;
        }
    }
}
