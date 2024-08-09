package com.in_u_in.in_u_in.user.service;

import com.in_u_in.in_u_in.common.jwt.JwtTokenProvider;
import com.in_u_in.in_u_in.common.jwt.SecurityUtil;
import com.in_u_in.in_u_in.common.jwt.TokenInfo;
import com.in_u_in.in_u_in.common.response.ApiResponse;
import com.in_u_in.in_u_in.common.response.code.ErrorCode;
import com.in_u_in.in_u_in.common.response.code.SuccessCode;
import com.in_u_in.in_u_in.common.response.exception.handler.ErrorHandler;
import com.in_u_in.in_u_in.user.dto.UserLoginRequestDTO;
import com.in_u_in.in_u_in.user.dto.UserResponseDTO;
import com.in_u_in.in_u_in.user.dto.UserSignUpRequestDto;
import com.in_u_in.in_u_in.user.entity.Authority;
import com.in_u_in.in_u_in.user.entity.Member;
import com.in_u_in.in_u_in.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ApiResponse signUp(UserSignUpRequestDto signUp) {
        if (memberRepository.existsByEmail(signUp.getEmail())) {
            throw new ErrorHandler(ErrorCode.EMAIL_ALREADY_EXIST);
        }
        Member user = Member.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .nickname(signUp.getNickname())
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        memberRepository.save(user);
        return ApiResponse.of(SuccessCode._SIGNUP_SUCCESS, "회원가입 성공!");
    }

    public ApiResponse login(UserLoginRequestDTO userLoginRequestDTO) {
        if (memberRepository.findByEmail(userLoginRequestDTO.getEmail()).orElse(null) == null) {
            throw new ErrorHandler(ErrorCode.MEMBER_NOT_FOUND);
        }
        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
            UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDTO.toAuthentication();

            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            Member member = memberRepository.findByEmail(userLoginRequestDTO.getEmail())
                    .orElseThrow();
            memberRepository.save(member);
            return ApiResponse.of(SuccessCode._LOGIN_SUCCESS, UserResponseDTO.builder().tokenInfo(tokenInfo).nickName(member.getNickname())
                    .build());
        } catch (AuthenticationException e) {
            // Handle authentication failure, e.g., incorrect password
            throw new ErrorHandler(ErrorCode.MEMBER_LOGIN_FAILURE);
        }
    }


//    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
//        // 1. Refresh Token 검증
//        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
//            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        // 2. Access Token 에서 User email 을 가져옵니다.
//        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());
//
//        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
//        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
//        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
//        if(ObjectUtils.isEmpty(refreshToken)) {
//            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
//        }
//        if(!refreshToken.equals(reissue.getRefreshToken())) {
//            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        // 4. 새로운 토큰 생성
//        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//
//        // 5. RefreshToken Redis 업데이트
//        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
//
//        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
//    }


    /*
    public ResponseEntity<?> authority() {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));
        // add ROLE_ADMIN
        member.getRoles().add(Authority.ROLE_ADMIN.name());
        memberRepository.save(member);

        return response.success();
    }
     */


    public Member getMemberFromToken() {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ErrorHandler(ErrorCode.UNAUTHORIZED));
        return member;
    }

}
