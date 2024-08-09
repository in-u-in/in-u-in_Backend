package com.in_u_in.in_u_in.user.dto;


import com.in_u_in.in_u_in.common.jwt.TokenInfo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDTO {

    TokenInfo tokenInfo;
    String nickName;
}
