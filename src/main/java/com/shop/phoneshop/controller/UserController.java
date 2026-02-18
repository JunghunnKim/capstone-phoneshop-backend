package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.*;
import com.shop.phoneshop.model.Role;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    /// 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return "회원가입 성공";
    }

    /// 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    /// 비밀번호 찾기
    @PostMapping("/password/reset")
    public String resetPassword(@RequestBody PasswordResetRequest request) {
        return userService.resetPassword(request);
    }

    /// 회원 정보 수정
    @PutMapping("/update")
    public String updateUser(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UpdateUserRequest request
    ) {
        Long userId = extractUserId(authorization);
        return userService.updateUser(userId, request);
    }

    /// 회원 삭제
    @DeleteMapping("/withdraw")
    public String withdraw(@RequestBody WithdrawRequest request) {
        return userService.withdraw(request);
    }

    /// JWT에서 userId 추출
    private Long extractUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getUserId(token);
    }

    private Role extractRole(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getRole(token);
    }
}
