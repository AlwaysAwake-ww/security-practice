package com.test.fakeapitest.repository;


import com.test.fakeapitest.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberIdAndDate(Long memberId, String date);


}
