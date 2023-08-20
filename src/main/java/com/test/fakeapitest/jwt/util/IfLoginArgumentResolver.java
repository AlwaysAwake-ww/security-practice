package com.test.fakeapitest.jwt.util;

import com.test.fakeapitest.jwt.token.JwtAuthenticationToken;
import io.jsonwebtoken.Jwt;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;
import java.util.Iterator;

public class IfLoginArgumentResolver implements HandlerMethodArgumentResolver {

    // HandlerMethodArgumentResolver : 컨트롤러 메서드에서 특정 조건에 맞는 매개변수가 있을 경우 원하는 값을 바인딩 해주는 인터페이스
    // @RequestBody 를 사용해서 Request 의 Body 값을 받아올 때, @PathVariable 사용해서 Request 의 Path parameter 를 받아올 때 이 HandlerMethodArgumentResolver 를 사용


    // 현재 parameter 를 resolver 가 지원하는지 boolean 반환
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // parameter.getParameterAnnotation : parameter 의 어노테이션이 @IfLogin 이 맞는지
        // parameger.getParameterType : parameter 의 type 이 LoginUserDto 가 맞는지
        return parameter.getParameterAnnotation(IfLogin.class) != null && parameter.getParameterType() == LoginUserDto.class;
    }

    // 실제로 바인딩 할 객체를 반환
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = null;

        try{
            
            // SercurityContextHolder 에서 authentication 정보 불러와 authentication 변수로 저장
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        catch(Exception e){
            return null;
        }

        if(authentication == null){
            return null;
        }

        // 가져온 인증 정보를 JwtAuthenticationToken 에 저장 (Token 문자열, Credential, Principal)
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        // LoginUserDto 객체 생성
        LoginUserDto loginUserDto = new LoginUserDto();

        // Object 타입 principal 객체 불러오기
        Object principal = authentication.getPrincipal();

        // principal 이 null 이면 null 값 반환
        if(principal == null)
            return null;



        // LoginUserInfoDto 생성 후 principal 로 초기화
        // principal : email
        LoginInfoDto loginInfoDto = (LoginInfoDto) principal;

        // email, memberId, name 정보 set
        loginUserDto.setEmail(loginUserDto.getEmail());
        loginUserDto.setMemberId(loginInfoDto.getMemberId());
        loginUserDto.setName(loginInfoDto.getName());


        // authentication 에서  authority Collection 불러오기
        // <? extends ABC> : ABC 또는 ABC 의 하위 ::: ? 는 와일드카드
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // authorities 콜렉션의 iterator 선언, 초기화
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        // authority 반목문으로 탐색
        while(iterator.hasNext()){

            GrantedAuthority grantedAuthority = iterator.next();

            // grantedAuthority 에서 가져온 Authority 를 role 문자열로 저장
            String role = grantedAuthority.getAuthority();

            // 반복문 내에서 loginUserDto 에 authority 에서 가져온 Authority add
            loginUserDto.addRole(role);
        }

        // loginUserDto 반환
        return loginUserDto;
    }
}
