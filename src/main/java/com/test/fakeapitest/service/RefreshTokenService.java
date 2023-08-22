package com.test.fakeapitest.service;


import com.test.fakeapitest.domain.RefreshToken;
import com.test.fakeapitest.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken addRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken){

        // ifPresent :: Optional 객체가 값을 가지고 있으면 실행, 없으면 스킵
        // refreshTokenRepository :: delete
        // (token)->refreshTokenRepository.delete(token)
        refreshTokenRepository.findByValue(refreshToken).ifPresent(refreshTokenRepository::delete);
//        refreshTokenRepository.findByValue(refreshToken).ifPresent(token->refreshTokenRepository.delete(token));
    }

    @Transactional
    public Optional<RefreshToken> findRefreshToken(String refreshToken){
        return refreshTokenRepository.findByValue(refreshToken);
    }
}
