package com.test.fakeapitest.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.fakeapitest.controller.MemberController;
import com.test.fakeapitest.domain.Member;
import com.test.fakeapitest.dto.MemberLoginDto;
import com.test.fakeapitest.dto.MemberLoginResponseDto;
import com.test.fakeapitest.dto.MemberSignupDto;
import com.test.fakeapitest.dto.MemberSignupResponseDto;
import com.test.fakeapitest.service.MemberService;
import com.test.fakeapitest.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class MemberTest {

    // 단위테스트에 대해 공부하기



    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;

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
}
