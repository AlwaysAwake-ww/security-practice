package com.test.fakeapitest.jwt.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenizer {


    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30 minutes
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days



    // @Value : spring 으로 properties를 읽는 방법 중 하나
    // application.yml 에 미리 jwt.secretKey, jwt.refreshKey 값을 설정해놓고 여기서 사용
    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret, @Value("${jwt.refreshKey}") String refreshSecret){

        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }


    // id, email, roles 를 가지고 AccessToken 생성
    public String createAccessToken(Long id, String email, List<String> roles){

        // id, email, roles, 토큰 만료 시간, 암호화 byte를 담아 토큰 생성
        return createToken(id, email, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret);
    }

    
    // 위와 마찬가지로 refreshToken 생성
    public String createRefreshToken(Long id, String email, List<String> roles) {
        return createToken(id, email, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret);
    }



    // id, email, roles, expire(만료시간), secretKey 를 가지고 토큰 생성
    public String createToken(Long id, String email, List<String> roles, Long expire, byte[] secretKey){

        // jwt claim 을 만들고 subject 를 설정
        // sub는 미리 정의된 claim 이기 때문에 setSubject 로 설정 가능
        // subject 는 의미 그대로 토큰의 제목? 이름? 이라고 보면 됨
        // email (고유한 값)으로 이 토큰의 이름을 설정하는 것
        Claims claims = Jwts.claims().setSubject(email);

        // roles 라는 private claim 삽입
        claims.put("roles", roles);
        claims.put("id", id);

        // 새 Jwt를 빌드
        return Jwts.builder()
                // 위에 만들고 설정한 claim 으로 설정
                .setClaims(claims)
                // 줄여서 iat : 토큰이 발급 된 시간 설정
                .setIssuedAt(new Date())
                // 줄여서 exp : 토큰 만료 시간, 발급 시간에 expire 매개변수로 받은 유지 시간을 더해서 언제 만료될 지 설정
                .setExpiration(new Date(new Date().getTime() + expire))
                // 토큰의 서명 부분
                // getSigningKey 메서드에 seceretKey 를 넣어 반환된 값을 Jwt 의 서명으로 활용
                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                // deprecate 되서 상위 버전에서는 string 값이 아니라 직접 Key를 생성하고 서명 진행해야 됨
                // String str = "MyNickNameisErjuerAndNameisMinsu" 값을 byte 형변환
                // Key key = Keys.hmacShaKeyFor("MyNickNameisErjuerAndNameisMinsu".getBytes(StandardCharsets.UTF_8));
                ////////////////////////////////////////////////////////////////////////////////////////////////////////
                .signWith(getSigningKey(secretKey))
                // 서명 완료 된 jwt를 생성하는 메서드
                .compact();
    }



    // token 에서 User ID 얻는 메서드
    public Long getUserIdFromToken(String token){
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];

        Claims claims = parseToken(token, accessSecret);

        return Long.valueOf((Integer)claims.get("userId"));
    }


    // secretKey 를 활용하여 Access Token 암호화
    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    // secretKey 를 활용하여 Refresh Token 암호화
    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }


    // token 문자열과 secretKey 코드를 받아서 jwt 검증
    public Claims parseToken(String token, byte[] secretKey){

        return Jwts
                // parserBuilder : 검증을 위한 jwtParserBuilder 인스턴스 생성
                .parserBuilder()
                // jws 서명을 확인하는데 사용할 secretKey 지정
                .setSigningKey(getSigningKey(secretKey))
                // 생성
                .build()
                // parseClaimsJws : 오리지널 signed Jwt 반환
                .parseClaimsJws(token)
                .getBody();
    }

    // ????
    // secretKey 값을 받아 Key 인스턴스를 생성하여 반환
    // 암호화 알고리즘
    public static Key getSigningKey(byte[] secretKey){

        return Keys.hmacShaKeyFor(secretKey);
    }
}
