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



    // 인증 완료된 객체
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credential) {
        super(authorities);
        this.credential = credential;
        this.principal = principal;
        super.setAuthenticated(true);

    }

    // 인증 전의 객체
    public JwtAuthenticationToken(String token){
        super(null);

        this.token = token;
        super.setAuthenticated(false);
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
