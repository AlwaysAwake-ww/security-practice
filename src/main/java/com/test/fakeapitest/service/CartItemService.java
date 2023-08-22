package com.test.fakeapitest.service;


import com.test.fakeapitest.domain.Cart;
import com.test.fakeapitest.domain.CartItem;
import com.test.fakeapitest.dto.AddCartItemDto;
import com.test.fakeapitest.repository.CartItemRepository;
import com.test.fakeapitest.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public CartItem addCartItem(AddCartItemDto addCartItemDto){

        Cart cart = cartRepository.findById(addCartItemDto.getCartId()).orElseThrow();

        CartItem cartItem = new CartItem();

        cartItem.setProductId(addCartItemDto.getProductId());
        cartItem.setProductPrice(addCartItemDto.getProductPrice());
        cartItem.setProductTitle(addCartItemDto.getProductTitle());
        cartItem.setProductDescription(addCartItemDto.getProductDescription());
        cartItem.setQuantity(addCartItemDto.getQuantity());
        cartItem.setCart(cart);

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public boolean isCartItemExist(Long memberId, Long cartItemId){

        return cartItemRepository.existsByCart_memberIdAndId(memberId, cartItemId);
    }

    @Transactional
    public CartItem updateCartItem(CartItem cartItem){


        CartItem updateCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();
        updateCartItem.setQuantity(cartItem.getQuantity());
        return updateCartItem;
    }

    @Transactional
    public CartItem getCartItem(Long memberId, Long cartId, Long productId){

        return cartItemRepository.findByCart_memberIdAndCart_idAndProductId(memberId, cartId, productId).orElseThrow();
    }

    @Transactional
    public void deleteCartItem(Long memberId, Long cartItemId){

        cartItemRepository.deleteByCart_memberIdAndId(memberId, cartItemId);
    }
}
