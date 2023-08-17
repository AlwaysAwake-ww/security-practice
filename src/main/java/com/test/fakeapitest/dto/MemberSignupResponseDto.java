package com.test.fakeapitest.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
// @Data : @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode 설정을 한꺼번에 해주는 어노테이션
// @ToString : 변수값들을 리턴해주는 toString() 메서드 자동 생성
// @EqualsAndHashCode : equals, hashCode 메서드 자동 생성
public class MemberSignupResponseDto {

    private Long memberId;
    private String email;
    private String name;
    private LocalDateTime regdate;
}
