package com.test.fakeapitest.controller;


import com.test.fakeapitest.domain.Cart;
import com.test.fakeapitest.jwt.util.IfLogin;
import com.test.fakeapitest.jwt.util.LoginUserDto;
import com.test.fakeapitest.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartApiController {


    // 현재 login 된 memberId 의 carts 정보
    // 있으면 carts 정보, 없으면 새로 생성
    // Body 에 Json 으로 memberId


    private final CartService cartService;

    @PostMapping
    public Cart addCarts(@IfLogin LoginUserDto loginUserDto){

        // @IfLogin >> 수동 어노테이션 설정하고, 권한 정보 가져오기

        LocalDate localDate = LocalDate.now();

        localDate.getYear();
        localDate.getMonth();
        localDate.getDayOfMonth();
        localDate.getMonthValue();

        // 월, 일 값이 1자리 수 일때 앞에 0 붙이기 
        // ex) 2월 5일이면 0205 로 나오도록
        String date = String.valueOf(localDate.getYear()) + (localDate.getMonthValue() < 10 ? "0" :"") + String.valueOf(localDate.getMonthValue()) + (localDate.getDayOfMonth() < 10 ? "0" :"") +String.valueOf(localDate.getDayOfMonth());
        Cart cart = cartService.addCart(loginUserDto.getMemberId(), date);
        return cart;
    }
}
