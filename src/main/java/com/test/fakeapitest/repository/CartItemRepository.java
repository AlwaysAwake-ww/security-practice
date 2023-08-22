package com.test.fakeapitest.repository;


import com.test.fakeapitest.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    boolean existsByCart_memberIdAndId(Long memberId, Long cartItemId);

    Optional<CartItem> findByCart_memberIdAndCart_idAndProductId(Long memberId, Long cartId, Long productId);

    void deleteByCart_memberIdAndId(Long memberId, Long cartItemId);
}
