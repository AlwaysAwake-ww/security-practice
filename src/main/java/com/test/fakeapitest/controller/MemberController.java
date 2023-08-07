package com.test.fakeapitest.controller;


import com.test.fakeapitest.dto.MemberLoginDto;
import com.test.fakeapitest.dto.MemberLoginResponseDto;
import com.test.fakeapitest.jwt.util.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDto loginDto){

        // TODO email 에 해당하는 사용자 정보를 읽어와서 암호가 맞는지 검사하는 코드 필요
        Long memberId = 1L;
        String email = loginDto.getEmail();
        List<String> roles = List.of("ROLE_USER");

        // jwt 토큰 생성. jwt 라이브러리 이용하여 생성
        String accessToken = jwtTokenizer.createAccessToken(memberId, email, roles);
        String refreshToken = jwtTokenizer.createRefreshToken(memberId, email, roles);

        MemberLoginResponseDto loginResponseDto = MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(memberId)
                .nickname("nickname")
                .build();

        return new ResponseEntity(loginResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token){

        // token repository 에서 refresh token 에 해당하는 값을 삭제
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
