package com.test.fakeapitest.controller;


import com.test.fakeapitest.domain.CartItem;
import com.test.fakeapitest.dto.AddCartItemDto;
import com.test.fakeapitest.jwt.util.IfLogin;
import com.test.fakeapitest.jwt.util.LoginUserDto;
import com.test.fakeapitest.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    
    // Cart 에 CartItem 추가
    @PostMapping
    public CartItem addCartItem(@IfLogin LoginUserDto loginUserDto, @RequestBody AddCartItemDto addCartItemDto){

        // Cart 에 이미 들어있는 상품이라면
        if(cartItemService.isCartItemExist(loginUserDto.getMemberId(), addCartItemDto.getProductId())){

            // quantity + 1
            CartItem cartItem = cartItemService.getCartItem(loginUserDto.getMemberId(), addCartItemDto.getCartId(), addCartItemDto.getProductId());
            cartItem.setQuantity(cartItem.getQuantity()+addCartItemDto.getQuantity());

            return cartItemService.updateCartItem(cartItem);
        }
        // 아니라면
        return cartItemService.addCartItem(addCartItemDto);
    }

    
    // 삭제
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity deleteCartItem(@IfLogin LoginUserDto loginUserDto, @PathVariable Long cartItemId){

        
        cartItemService.deleteCartItem(loginUserDto.getMemberId(), cartItemId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
