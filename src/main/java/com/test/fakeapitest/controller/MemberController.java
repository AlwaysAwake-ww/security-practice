package com.test.fakeapitest.controller;


import com.test.fakeapitest.domain.Member;
import com.test.fakeapitest.domain.RefreshToken;
import com.test.fakeapitest.domain.Role;
import com.test.fakeapitest.dto.*;
import com.test.fakeapitest.jwt.util.JwtTokenizer;
import com.test.fakeapitest.service.MemberService;
import com.test.fakeapitest.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberController {

    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid MemberSignupDto memberSignupDto, BindingResult bindingResult){

        // BindingResult : 검증 오류를 보관하는 객체


        if(bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Member member = new Member();
        member.setName(memberSignupDto.getName());
        member.setEmail(memberSignupDto.getEmail());
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        Member saveMember = memberService.addMember(member);

        MemberSignupResponseDto memberSignupResponse = new MemberSignupResponseDto();
        memberSignupResponse.setMemberId(saveMember.getMemberId());
        memberSignupResponse.setName(saveMember.getName());
        memberSignupResponse.setEmail(saveMember.getEmail());
        memberSignupResponse.setRegdate(saveMember.getRegdate());

        return new ResponseEntity(memberSignupResponse, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDto loginDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.findByEmail(loginDto.getEmail());

        // password 매칭
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<String> roles = member.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        // jwt 토큰 생성. jwt 라이브러리 이용하여 생성
        String accessToken = jwtTokenizer.createAccessToken(member.getMemberId(), member.getEmail(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getMemberId(), member.getEmail(), roles);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setMemberId(member.getMemberId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        MemberLoginResponseDto loginResponseDto = MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(member.getMemberId())
                .nickname(member.getName())
                .build();

        return new ResponseEntity(loginResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestBody RefreshTokenDto refreshTokenDto){

        // refresh token 을 삭제
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());

        // token repository 에서 refresh token 에 해당하는 값을 삭제
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody RefreshTokenDto refreshTokenDto){

        // 1. 전달받은 유저의 아이디로 유저가 존재하는지 확인한다.
        // 2. RefreshToken이 유효한지 체크한다.
        // 3. AccessToken을 발급하여 기존 RefreshToken과 함께 응답한다.

        // Optional 안에 담긴 RefershToken 을 반환
        // orElseThrow :: 예외 처리, IllegalArgumentException 발생 시, 에러 메세지 전달
        // catch(Exception e){ throw new IllegalArgumentException("메세지"); }  과 동일한 동작
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenDto.getRefreshToken()).orElseThrow(()->new IllegalArgumentException("refresh token not found"));

        // claim :: jwt 토큰의 payload 에 담긴 정보들 중 한 조각 단위
        // jwtTokenizer 로 refreshToken 을 암호화하여 반환되는 claim 저장
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken.getValue());

        // userId 의 내용을 불러와서 저장

        Long userId = Long.valueOf((Integer)claims.get("id"));

        // userId 를 바탕으로 db에 저장된 member 변수 불러오기
        Member member = memberService.getMember(userId).orElseThrow(()->new IllegalArgumentException("Member not found"));


        // claim 의 roles 정보를 리스트에 저장
        List<String> roles = (List)claims.get("roles");
//        Object roles = claims.get("roles");


        // claim 의 제목 (Subject) 이 email 정보로 설정되어 있기 때문에 Subject를 불러와 email 에 저장
        String email = claims.getSubject();

        // userId, email, roles 정보로 새로운 accessToken 생성
        String accessToken = jwtTokenizer.createAccessToken(userId, email, roles);

        MemberLoginResponseDto memberLoginResponseDto = MemberLoginResponseDto.builder()

                .accessToken(accessToken)
                .refreshToken(refreshToken.getValue())
                .memberId(member.getMemberId())
                .nickname(member.getName())
                .build();
        return new ResponseEntity(memberLoginResponseDto, HttpStatus.OK);
    }
}
