package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.*;
import com.shop.phoneshop.model.User;
import com.shop.phoneshop.repository.UserRepository;
import com.shop.phoneshop.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /// 회원가입
    public void signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );

        userRepository.save(user);
    }

    /// 로그인
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요."
                        )
                );

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException(
                    "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요."
            );
        }

        String token = jwtTokenProvider.createToken(user.getId());
        return new LoginResponse(token);
    }

    /// 비밀번호 찾기(임시비밀번호 반환)
    public String resetPassword(PasswordResetRequest request) {

        User user = userRepository
                .findByEmailAndName(request.getEmail(), request.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException("회원 정보가 일치하지 않습니다.")
                );

        String tempPassword = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        user.changePassword(tempPassword);

        userRepository.save(user);

        return "임시 비밀번호: " + tempPassword;
    }

    /// 회원정보 수정
    @Transactional
    public String updateUser(Long userId, UpdateUserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 회원입니다.")
                );

        user.updateInfo(
                request.getName(),
                request.getPassword()
        );

        return "회원정보 수정 완료";
    }

    /// 회원 탈퇴
    public String withdraw(WithdrawRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 회원입니다.")
                );

        // 이름 검증
        if (!user.getName().equals(request.getName())) {
            throw new IllegalArgumentException("회원 정보가 일치하지 않습니다.");
        }

        // 비밀번호 검증
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("회원 정보가 일치하지 않습니다.");
        }

        userRepository.delete(user);

        return "회원 탈퇴 완료";
    }

}
