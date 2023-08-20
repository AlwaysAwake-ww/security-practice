package com.test.fakeapitest.config;


import com.test.fakeapitest.jwt.util.IfLoginArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Spring MVC 에 대한 설정 파일

    // cors 에 대한 설정
    // 프론트엔드 3000번 포트, 백엔드 8080 포트
    // http://localhost:3000 ---> 8080 api 호출할 수 있도록 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTION")
                .allowCredentials(true);
    }

    // 커스텀 argumentResolver 사용하기 위한 메서드 오버라이딩 
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new IfLoginArgumentResolver());
    }
}
