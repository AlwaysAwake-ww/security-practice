package com.test.fakeapitest.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokenDto {

    @NotEmpty
    String refreshToken;
}
