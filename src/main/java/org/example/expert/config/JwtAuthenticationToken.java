package org.example.expert.config;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final String userId;
    private final String email;
    @Getter
    private final String userRole;
    @Getter
    private final String nickname;

    public JwtAuthenticationToken(
            String userId,
            String email,
            String userRole,
            String nickname) {
        super(Collections.singletonList(new SimpleGrantedAuthority(userRole)));
        this.userId = userId;
        this.email = email;
        this.userRole = userRole;
        this.nickname = nickname;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }
}
