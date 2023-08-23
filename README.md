Spring Security, Jwt 인증 과정


![security drawio](https://github.com/AlwaysAwake-ww/shoppingmallapi/assets/32862865/debc1b5d-8b7c-42e6-9e05-6ffca9c3e26e)


 

1. request 전달

** UsernamePasswordAuthenticationFilter 실행 전에 JwtAuthenticationFilter 실행
UsernamePasswordAuthenticationFilter 는 Override 되지 않고 JwtAuthenticationFilter 의 인증 처리가 완료 되면 자연스럽게 통과

2. JwtAuthenticationFilter 에서 요청받은 request 에 담긴 Token 을 바탕으로 JwtAuthenticationToken 생성 (인증 전 객체)

3. AuthenticationManager 에게 JwtAuthenticationToken 객체 전달

4. AuthenticationManager 는 적절한 Provider 탐색 (여기서는 JwtAuthenticationProvider)

** JwtAuthenticationToken 을 Authentication 으로 변환하여 탐색

5. JwtAuthenticationProvider 는 전달받은 Authentication 객체를 다시 JwtAuthenticationToken 으로 변환하고
JwtAuthenticationToken 의 Claim 안에 들어있는 accessToken, email, name, memberId, role 정보를 loginInfoDto 에 저장


6. role 과 인증된 user 정보가 담긴 loginInfoDto 를 담고있는 JwtAuthenticationToken 생성 (인증 완료 된 객체)
인증 완료된 JwtAuthenticationToken 객체를 반환

** JwtAuthenticationToken 객체는 String token 으로 생성하면 인증 false
authorities 와 loginInfoDto 를 담아 생성하면 인증 true

7. JwtAuthenticationFilter 는 인증 완료된 Authenticate 객체를 SecurityContextHolder 의 Context 의 Authentication 으로 저장

8. 인증 완료

