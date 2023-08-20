package com.test.fakeapitest.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fakeapitest.domain.Member;
import com.test.fakeapitest.dto.*;
import com.test.fakeapitest.repository.RefreshTokenRepository;
import com.test.fakeapitest.service.MemberService;
import com.test.fakeapitest.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class MemberTest {

    // 단위테스트에 대해 공부하기



    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    // 테스트용 mvc 환경
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    // 회원가입 테스트 (정상 동작)
    @Test
    public void signupTest() throws Exception{
        String memberEmail = "test@gmail.com";
        String memberName = "test_member";
        String memberPassword = "*test1234";

        Member member = new Member();
        member.setEmail(memberEmail);
        member.setName(memberName);
        member.setPassword(passwordEncoder.encode(memberPassword));

        Member saveMember = memberService.addMember(member);
        Member findMember = memberService.findByEmail(memberEmail);

        Assertions.assertThat(saveMember.getEmail()).isEqualTo(findMember.getEmail());
    }



    // 로그인 테스트
    @Test
    public void loginTest() throws Exception{

        String memberEmail = "test@gmail.com";
        String memberPassword = "*test1234";

        MemberLoginDto memberLoginDto = new MemberLoginDto(memberEmail, memberPassword);


        mockMvc
                // parameter 세팅
                // memberLoginDto 객체를 Json 변환하여 사용
                .perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(memberLoginDto))
                )
                // response status 200 이 나오길 기대
                .andExpect(status().isOk())
                .andReturn();
    }

    // Object to String
    private <T> String toJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }


    // logout 테스트
    @Test
    public void logoutTest() throws Exception{


        Long memberId = 9L;

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken(refreshTokenRepository.findByMemberId(memberId).get().getValue());

        mockMvc
                .perform(delete("/members/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(refreshTokenDto))
                )
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void accessTokenRefreshTest() throws Exception{

        String memberEmail = "test@gmail.com";
        String memberPassword = "*test1234";

        MemberLoginDto memberLoginDto = new MemberLoginDto(memberEmail, memberPassword);

        Long memberId = memberService.findByEmail(memberEmail).getMemberId();

        mockMvc
                .perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(memberLoginDto))
                )
                .andExpect(status().isOk())
                .andReturn();


        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setRefreshToken(refreshTokenRepository.findByMemberId(memberId).get().getValue());

        mockMvc
                .perform(post("/members/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(refreshTokenDto))
                )
                .andExpect(status().isOk())
                .andReturn();

    }
}
