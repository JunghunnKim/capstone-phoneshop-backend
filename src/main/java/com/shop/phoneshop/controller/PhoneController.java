package com.shop.phoneshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.phoneshop.dto.PhoneCreateRequest;
import com.shop.phoneshop.dto.PhoneDetailResponse;
import com.shop.phoneshop.dto.PhoneResponse;
import com.shop.phoneshop.exception.AdminOnlyException;
import com.shop.phoneshop.exception.ForbiddenException;
import com.shop.phoneshop.model.Role;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phones")
public class PhoneController {

    private final PhoneService phoneService;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    /// 핸드폰 등록
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPhone(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("phone") String phoneJson,
            @RequestPart("image") MultipartFile image
    ) throws Exception {

        validateAdmin(authorization);

        PhoneCreateRequest request =
                objectMapper.readValue(phoneJson, PhoneCreateRequest.class);

        phoneService.createPhone(request, image);
    }


    /// 전체 핸드폰 조회
    @GetMapping
    public List<PhoneResponse> getAllPhones() {
        return phoneService.getAllPhones();
    }

    /// 특정 핸드폰 조회
    @GetMapping("/{id}")
    public PhoneDetailResponse getPhone(@PathVariable Long id) {
        return phoneService.getPhoneById(id);
    }

    /// 특정 핸드폰 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhone(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        validateAdmin(authorization);
        phoneService.deletePhone(id);
    }

    private Role extractRole(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getRole(token);
    }

    private void validateAdmin(String authorization) {
        Role role = extractRole(authorization);
        if (role != Role.ADMIN) {
            throw new AdminOnlyException();
        }
    }
}
