package com.in_u_in.in_u_in.user.controller;

import com.in_u_in.in_u_in.common.response.ApiResponse;
import com.in_u_in.in_u_in.user.dto.UserLoginRequestDTO;
import com.in_u_in.in_u_in.user.dto.UserSignUpRequestDto;
import com.in_u_in.in_u_in.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService usersService;

    @CrossOrigin
    @Operation(summary = "회원가입 API")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody  @Valid UserSignUpRequestDto userSignUpRequestDto) {
        return usersService.signUp(userSignUpRequestDto);
    }
    @CrossOrigin
    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {
        return usersService.login(userLoginRequestDTO);
    }

}
