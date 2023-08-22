package com.test.fakeapitest.jwt.filter;


import com.test.fakeapitest.jwt.exception.JwtExceptionCode;
import com.test.fakeapitest.jwt.token.JwtAuthenticationToken;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;
import java.util.Base64;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Jwt 예외 처리를 위한 filter 클래스


    // Filter : Servlet Interface
    // servlet context의 기능, 사용자에 의해 서블릿 호출 전/후로 request/response의 헤더 정보 등 검사 가능
    // 코드1 > 서블릿 실행 (chain.doFilter) > 코드2  순서로 실행
    // 여기서 코드1은 서블릿 이전, 2는 서블릿 이후에 실행
    // 서블릿 실행하는 동안 다른 서블릿에 대한 요청 존재할 수 있음 >> 다른 서블릿에도 동일한 필터 존재 >> 필터 다시 실행됨

    // OncePerRequestFilter : 한 번의 요청에 대해 정확히 한 번만 실행되는 필터 >> 보안 인증 작업으로 유용

    private final AuthenticationManager authenticationManager;




    //
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = "";

        try{

            // request 에서 토큰 문자열만 뽑아 저장
            token = getToken(request);

            // token 에 문자열이 저장 되어 있다면
            if(StringUtils.hasText(token)){

                // 인증 정보 가져와서
                getAuthentication(token);
            }
            // 필터 수행
            filterChain.doFilter(request, response);
        }

        // NullPointerException : 값이 null 일 때
        // IllegalStateException : 사용자는 값을 입력했지만, 코드가 처리할 준비가 안 된 경우(객체 상태가 메서드 호출을 처리하기에 적합하지 않은 상태)
        catch (NullPointerException | IllegalStateException e) {
            // request의 exception 속성에 JwtExceptionCode.NOT_FOUND_TOKEN.getCode(), 즉, "NOT_FOUND_TOKEN" 라는 코드 get
            request.setAttribute("exception", JwtExceptionCode.NOT_FOUND_TOKEN.getCode());

            // slf4j 로그
            // 로깅 처리 방식
            // format 문자열에 중괄호를 넣고 그 순서대로 출력하고자 하는 데이터를 "," 로 구분하여 전달
            log.error("Not found Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            // BadCredentialException : 사용자 ID가 전달되지 않았거나 인증 저장소의 사용자 ID에 해당하는 PW가 일치하지 않을 경우 발생
            throw new BadCredentialsException("throw new not found token exception");
        }

        // SecurityException : 보안 에러
        // MalformedJwtException : 토큰값이 옳바르지 않을 때
        catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
            log.error("Invalid Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new invalid token exception");
        }

        // ExpiredJwtException : 기간만료
        catch (ExpiredJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
            log.error("EXPIRED Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new expired token exception");
        }

        // UnsupportedJwtException : 지원하지 않는 토큰
        catch (UnsupportedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
            log.error("Unsupported Token // token : {}", token);
            log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
            throw new BadCredentialsException("throw new unsupported token exception");
        }

        catch (Exception e) {
            log.error("====================================================");
            log.error("JwtFilter - doFilterInternal() error catch");
            log.error("token : {}", token);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.printStackTrace();
            log.error("}");
            log.error("====================================================");
            throw new BadCredentialsException("throw new exception");
        }
    }


    private void getAuthentication(String token){

        // token 정보를 담고있는 JwtAuthenticationToken 객체 생성
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);

        // authenticationManager.authenticate 메서드에 JwtAuthenticationToken 객체로 인증 수행
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // SecurityContextHolder 의 Authentication 값 설정
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }



    // 사용자 request 에서 token 가져오는 메서드
    private String getToken(HttpServletRequest request){

        // authorization 문자열에 request의 getHeader 메서드를 사용해서  Authorization 정보 가져와 저장
        String authorization = request.getHeader("Authorization");

        // StringUtils.hasText(내용) : 내용이 있을 경우 true, null 일 경우 false 반환
        // authorization 문자열에 Authorization 정보가 저장되어 있고, Bearer 로 시작한다면 (Bearer는 인증 타입이라고 보면 됨)
        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer")){
            
            // arr 배열에 authorization 문자열 공백 기준으로 잘라 삽입
            String[] arr = authorization.split(" ");
            // arr[0] 에는 "Bearer" 문자열이 들어가므로 arr[1] 의 토큰 내용 반환
            return arr[1];
        }

        // 조건 불만족 시 null 반환
        return null;
    }
}
