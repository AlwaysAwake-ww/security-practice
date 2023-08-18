package com.test.fakeapitest.repository;

import com.test.fakeapitest.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByValue(String value);

    Optional<RefreshToken> findByMemberId(Long id);
}
