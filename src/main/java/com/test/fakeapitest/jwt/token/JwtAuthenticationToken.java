package com.test.fakeapitest.jwt.token;


import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private Object principal;
    private Object credential;



    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credential) {
        super(authorities);
        this.credential = credential;
        this.principal = principal;
        this.setAuthenticated(true);

    }

    public JwtAuthenticationToken(String token){
        super(null);

        this.token = token;
        this.setAuthenticated(false);
    }


    @Override
    public Object getCredentials() {
        return this.credential;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
