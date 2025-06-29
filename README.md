# 📌 Spring Security + JWT 인증 연습 프로젝트

> Spring Security와 JWT토큰을 사용한 인증 과정 구현 연습 프로젝트

---

## ✅ 인증 플로우 요약

Spring Security 환경에서 JWT 기반 인증 처리를 적용한 구조입니다.  
`UsernamePasswordAuthenticationFilter` 전에 **사용자 정의 `JwtAuthenticationFilter`**를 등록하여 토큰 인증을 수행합니다.

---

## ⚙️ 인증 흐름

1. 📤 **Request 요청**
   - 클라이언트가 Authorization 헤더에 JWT 토큰 포함하여 요청
   - 요청은 `UsernamePasswordAuthenticationFilter` **이전에** `JwtAuthenticationFilter`에서 가로채어 처리됨

2. 🛡️ **JwtAuthenticationFilter**
   - 요청에서 JWT 토큰 추출
   - 추출한 토큰을 바탕으로 **인증 전 객체**인 `JwtAuthenticationToken` 생성
   - 이 객체를 `AuthenticationManager`로 전달

3. 🧠 **AuthenticationManager**
   - 전달받은 `JwtAuthenticationToken`을 처리 가능한 Provider 탐색
   - 이 경우 `JwtAuthenticationProvider` 선택

4. 🔍 **JwtAuthenticationProvider**
   - `JwtAuthenticationToken` 내부 토큰 값 파싱
   - Claim에서 `email`, `memberId`, `role`, `name` 등을 추출하여 `LoginInfoDto` 생성
   - 인증 완료 상태의 `JwtAuthenticationToken` 반환 (authorities 포함)

5. 🗝️ **SecurityContext 등록**
   - `JwtAuthenticationFilter`는 인증된 `JwtAuthenticationToken`을 받아
   - `SecurityContextHolder.getContext().setAuthentication(...)`에 저장

6. ✅ **인증 완료**
   - 이후 컨트롤러에서 `@AuthenticationPrincipal` 또는 `SecurityContextHolder`를 통해 사용자 정보 접근 가능

---

## 🔁 인증 객체 분류

| 객체 | 설명 |
|------|------|
| `JwtAuthenticationToken(token)` | 인증 전, `isAuthenticated = false` |
| `JwtAuthenticationToken(loginInfoDto, authorities)` | 인증 완료, `isAuthenticated = true` |

---

## 🧩 관련 구성 요소

- `JwtAuthenticationFilter`  
  → 인증 필터 (Spring Filter Chain 상에서 UsernamePasswordAuthenticationFilter 앞에 위치)

- `JwtAuthenticationProvider`  
  → 실제 인증 수행. Token 검증 후 사용자 정보 생성

- `JwtAuthenticationToken`  
  → 인증 객체. 인증 전/후 상태로 구분됨

- `LoginInfoDto`  
  → 인증 완료 후 사용자의 식별 정보(email, role 등)를 담는 객체

---

## 📊 인증 흐름 다이어그램

![security drawio](https://github.com/AlwaysAwake-ww/shoppingmallapi/assets/32862865/debc1b5d-8b7c-42e6-9e05-6ffca9c3e26e)

또는

```mermaid
sequenceDiagram
    participant Client
    participant JwtFilter
    participant AuthManager
    participant JwtProvider
    participant SecurityContext

    Client->>JwtFilter: Request with JWT
    JwtFilter->>AuthManager: JwtAuthenticationToken
    AuthManager->>JwtProvider: authenticate()
    JwtProvider-->>AuthManager: 인증된 JwtAuthenticationToken
    AuthManager-->>JwtFilter: 인증 성공
    JwtFilter->>SecurityContext: setAuthentication()






<!--
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

-->
