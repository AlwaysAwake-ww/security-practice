package com.test.fakeapitest.config;


import com.test.fakeapitest.jwt.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final AuthenticationManagerConfig authenticationManagerConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;



    // security filter chain
    // 인증을 처리하는 여러개의 시큐리티 필터를 담는 필터 체인
    // 어떤 시큐리티 필터를 통해 인증을 수행할 지 결정하는 역할
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                    // security 세션 정책
                    // Always : 시큐리티가 항상 세션 생성
                    // IF_REQUIRED : 필요시 생성 (default)
                    // NEVER : 시큐리티가 세션을 생성하지 않지만, 기존에 존재한다면 사용
                    // STATELESS : 시큐리티가 세션 생성하지도 않고 기존것을 사용하지도 않음
                    // jwt 토큰방식 사용할 거기 때문에 STATELESS 로 세션 안쓸거!
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    // 직접 ID, PW를 입력받고 jwt 토큰으로 리턴하는 API를 만들거기 때문에 Form Login 방식은 사용 X
                    .formLogin().disable()
                    // csrf : Cross Site Request Forgery 약자
                    // csrf 라는 공격을 막기 위한 설정 >> 복잡해서 disable
                    .csrf().disable()
                    // cors : Cross Origin Resource Sharing
                    // 서로 다른 출처의 애플리케이션들이 서로의 자원에 접근할 수 있도록 해준다
                    // PROTOCOL, PORT, HOST 를 통틀어 Origin(출처) 라고 함
                    // 같은 출처란 위 셋이 동일해야함, 하나라도 다르다면 cors, 교차출처가 되는것
                    // corsConfigurationSource(corsConfigurationSource()) 에서 설정?
                    .cors()
                    .and()
                    // 사용자 정의 API 사용 허가
                    .apply(authenticationManagerConfig)
                    .and()
                    // http basic 인증 비활성화
                    .httpBasic().disable()

                    // security 처리에 HttpServletRequest 이용하겠다
                    .authorizeRequests()
                    // Preflight request 허용
                    // Preflight request : 사전 요청, 본격적인 cors(교차출처) HTTP 요청 전에 서버측에서 그 요청의 메서드와 헤더에 대해 인식하고 있는지 체크하는 것
                    // 본 요청을 보내기 전에 요청에 대한 권한을 확인하여 본 요청이 유효한가 아닌가 체크 가능
                    // antMatchers() : 스프링 시큐리티에서 기본적으로 사용하는 antMatchers에서는 URI 맨 뒤에 '/' 가 붙으면 이를 검증하지 못함 (ex : "/api/members/" 와 같은 API URI 불가)
                    // 즉, URI가 정확하게 일치하는 경우에만 허용
                    // mvcMatchers() : HTTP Method를 파라미터로 넘겨주면 해당 메소드와 요청 URI 매핑, 아니라면 URI는 HTTP Method 신경 X
                    // requestMatchers() : 명확하게 요청 대상을 지정하는 경우
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    // 아래 기술된 URI 요청은 모두 허용
                    .mvcMatchers("/members/signup", "/members/login", "/members/refresh").permitAll()
                    // hasAnyRole : 사용자가 주어진 권한이 있다면 허용
                    .mvcMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                    .mvcMatchers(HttpMethod.POST, "/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                    // 아무 권한이나 가지고 있으면 모든 리소스 접근 허용 (?)
                    .anyRequest().hasAnyRole()
                    .and()
                    // 인증, 인가에서 Exception 발생 시 후처리
                    .exceptionHandling()
                    // 인증 예외 발생 시 사용할 커스텀 클래스 설정
                    .authenticationEntryPoint(customAuthenticationEntryPoint);


        return http.build();
    }



    // <Advanced> Security Cors 로 변경 시도
    public CorsConfigurationSource corsConfigurationSource(){


        // 프론트는 포트 3030, 백엔드는 포트 8080을 주로 사용
        // 다른 포트의 자원을 공유하기 위한 설정이라고 보면 됨
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTION", "PATCH", "PUT"));

        source.registerCorsConfiguration("/**", config);

        return source;
    }


    // password 암호화, 복호화를 위한 passwordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
