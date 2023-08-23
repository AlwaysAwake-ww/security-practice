package com.test.fakeapitest.config;


import com.test.fakeapitest.jwt.filter.JwtAuthenticationFilter;
import com.test.fakeapitest.jwt.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig extends AbstractHttpConfigurer<AuthenticationManagerConfig, HttpSecurity> {


    // 사용자 정의 API 사용 가능 커스텀 설정
    // 커스텀 설정을 위해서 AbstractHttpConfigurer 클래스 확장
    // @Bean 으로 configure 메소드 등록하여 사용하는 방법과 지금처럼 AbstractHttpConfigurer 클래스를 확장하고 configure 메소드 오버라이딩 하는 방법 두개 존재


    // jwt 인증 provider
    // 커스텀 설정 해야됨
    private final JwtAuthenticationProvider jwtAuthenticationProvider;


    // configure 메서드 확장
    // security 구성을 포함하는 메서드

    @Override
    public void configure(HttpSecurity builder) throws Exception {

        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);


        // addFilterBefore : 지정된 필터 앞에 커스텀 필터 추가
        // 매치 확인하기 전에 jwt provider 통해서 jwt에 대한 처리
        // UsernamePasswordAuthenticationFilter 보다 JwtAuthenticationProvider 를 먼저 실행시킨다는 의미
        // authenticationProvider : 인증 성공, 실패, 결정할 수 없음 등을 나타냄
        builder
                .addFilterBefore(
                        new JwtAuthenticationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                        .authenticationProvider(jwtAuthenticationProvider);

    }
}
