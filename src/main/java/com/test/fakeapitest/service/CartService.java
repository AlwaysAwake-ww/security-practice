package com.test.fakeapitest.service;


import com.test.fakeapitest.domain.Cart;
import com.test.fakeapitest.jwt.util.LoginUserDto;
import com.test.fakeapitest.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {


    private final CartRepository cartRepository;
    @Transactional
    public Cart addCart(Long memberId, String date){


        // cartRepositry 에서 memberId 와 date 를 기준으로 현재 cart 정보 불러오기
        Optional<Cart> cart  = cartRepository.findByMemberIdAndDate(memberId, date);

        // 만약 cart 내용이 비어있으면
        if(cart.isEmpty()){
            // 새 Cart 생성하고 memberId, date 를 현재 정보로 설정하고
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setDate(date);
            
            // cartRepository 에 newCart 를 저장
            Cart saveCart = cartRepository.save(newCart);
            return saveCart;
        }
        // cart 안에 내용이 있으면
        else{
            // Optional<Cart> 로 받은 cart 의 Cart 객체 반환
            return cart.get();
        }
    }
}
