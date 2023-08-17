package com.test.fakeapitest.util;


import com.test.fakeapitest.jwt.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class JwtTokenizerTest {

    // JwtTokenizer 동작 테스트
    // 임의의 토큰 생성, 읽기 테스트


    @Autowired
    JwtTokenizer jwtTokenizer;

    
    // application.yml 파일의 secretKey 활용
    @Value("${jwt.secretKey}")
    String accessSecret;

    // 만료 시간 30분
    public final Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L;


    // jwt token create test
    @Test
    public void createToken() throws Exception{

        String email = "urstory@gmail.com";
        List<String> roles = List.of("ROLE_USER");
        Long id = 1L;

        Claims claims = Jwts.claims().setSubject(email);

        claims.put("roles", roles);
        claims.put("userId", id);

        byte[] accessSecret = this.accessSecret.getBytes(StandardCharsets.UTF_8);
        System.out.println(":: this.accessSecret :: "+this.accessSecret);
        System.out.println(":: accessSecret :: "+accessSecret);


        // builder 패턴
        String JwtToken = Jwts.builder()
                // claims 가 추가된 JwtBuilder 리턴
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + this.ACCESS_TOKEN_EXPIRE_COUNT))
                // 결과에 서명까지 포함된 JwtBuilder 리턴
                .signWith(Keys.hmacShaKeyFor(accessSecret))
                // JwtToken 리턴
                .compact();

        System.out.println("::: Token Test ::: "+JwtToken);
    }






    // jwt token read test
    @Test
    public void parseTest() throws Exception{

        byte[] accessSecret = this.accessSecret.getBytes(StandardCharsets.UTF_8);

        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cnN0b3J5QGdtYWlsLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJ1c2VySWQiOjEsImlhdCI6MTY5MTQxNTA0OCwiZXhwIjoxNjkxNDE2ODQ4fQ.B_1hHg3F9W54IVFtpj9dbH-2JLmyD2LRzl3RgRKicXs";

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(accessSecret))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        System.out.println(":: claims.getSubject :: "+claims.getSubject());
        System.out.println(":: claims.get(roles) :: "+claims.get("roles"));
        System.out.println(":: claims.get(userId) :: "+claims.get("userId"));
        System.out.println(":: claims.getIssuedAt() :: "+claims.getIssuedAt());
        System.out.println(":: claims.getExpiration() :: "+claims.getExpiration());


    }
}
