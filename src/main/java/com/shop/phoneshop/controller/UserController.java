package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.*;
import com.shop.phoneshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/password/reset")
    public String resetPassword(@RequestBody PasswordResetRequest request) {
        return userService.resetPassword(request);
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody UpdateUserRequest request) {
        return userService.updateUser(request);
    }

    @DeleteMapping("/withdraw")
    public String withdraw(@RequestBody WithdrawRequest request) {
        return userService.withdraw(request);
    }

}
