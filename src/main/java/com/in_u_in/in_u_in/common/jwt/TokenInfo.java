package com.in_u_in.in_u_in.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
@AllArgsConstructor
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
}
