package com.test.fakeapitest.controller;


import com.test.fakeapitest.jwt.util.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class HelloApiController {


    private final JwtTokenizer jwtTokenizer;


    @GetMapping("/hello")
    public String hello(@RequestHeader("Authorization") String token){
        Long userIdFormToken = jwtTokenizer.getUserIdFromToken(token);
        return "hello" + userIdFormToken;
    }

}
