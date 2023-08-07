package com.test.fakeapitest.jwt.provider;


import com.test.fakeapitest.jwt.token.JwtAuthenticationToken;
import com.test.fakeapitest.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// configuration 파일에 따로 Bean 등록을 하지 않아도 사용 가능
// 타입 기반 자동 주입 어노테이션
// @Autowired, @Resource 와 비슷한 기능
// 개발자가 직접 작성한 Class를 Bean으로 등록하기 위한 어노테이션
// @ComponentScan 기능으로 스캔될 때, 패키지 내에서 @Component 어노테이션이 붙은 Class 식별, 그 Class의 Bean을 생성하여 ApplicationContext 에 등록
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    // AuthenticationProvider
    // DB에서 가져온 정보와 input 된 정보가 비교되서 체크되는 로직이 포함되어 있는 인터페이스


    private final JwtTokenizer jwtTokenizer;



    // Authenticate 메소드의 경우 Authentication Manager 에서 넘겨준 인증 객체(Authentication) 을 가지고 메소드 생성
    // 사용자 정보 담겨있음
    // 로그인 id, pw 정보를 객체에서 가지고 올 수 있음
    // DB에서 사용자 정보를 가져오려면 UserDetailsService 에서 리턴해주는 값을 가지고 사용 가능
    // UserDetailsService 의 경우 UserDetails를 리턴
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;

        // 토큰 검증
        // 기간 만료, 토큰 문자열의 문제 등등 Exception 발생
        // Claim : jwt 토큰의 payload 부분 안에 있는 내용, claim 은 엔티티와 추가 데이터에 대한 명세
        // registered claims : 상호 정보교환 가능하고 유용한 claim 을 제공하기 위해 필수적이진 않지만 권장되는 미리 정의된 claim
        // ex : iss(issuer), exp(expiration time), sub(subject), aud(audience) 등
        // public claims : jwt 사용자가 자유롭게 정의 가능한 claim, 충돌 방지 필요
        // private claims : public, registered가 아닌 claim, private를 사용하기 위해 협의된 당사자끼리 정보 교환하기 위한 사용자 정의 claim
        Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());

        // sub에 암호화 된 데이터를 집어넣고 복호화 하는 코드 삽입 가능
        // claim.getSubject() 를 통해 sub를 가져와서 email 문자열에 저장
        String email = claims.getSubject();
        List<GrantedAuthority> authorities = getGrantedAuthorities(claims);

        // JwtAuthenticationToken 에 인증된 authorities 와 email,  ~ 를 담아 생성 후 반환
        return new JwtAuthenticationToken(authorities, email, null);
    }


    public List<GrantedAuthority> getGrantedAuthorities (Claims claims) {

        
        // claims 에서 role 추출
        List<String> roles = (List<String>)claims.get("roles");
        // 권한 리스트
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 권한 리스트에 role 정보 삽입
        for(String role : roles){
            authorities.add(()->role);
        }
        // 권한 리스트 반환
        return authorities;
    }


    // Token 타입에 따라 언제 Provider 사용할지 조건 명시 가능
    @Override
    public boolean supports(Class<?> authentication) {

        // isAssignableFrom(authentication) : 특정 클래스가 어떤 클래스/인터페이스를 상속/구현 했는지 체크
        // JwtAuthenticationToken 클래스가 authentication 클래스를 상속/구현했는지 체크하여 true, false 반환
        // true 반환해야 인증 가능
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
