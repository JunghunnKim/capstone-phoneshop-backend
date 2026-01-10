package com.shop.phoneshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.phoneshop.dto.PhoneCreateRequest;
import com.shop.phoneshop.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phones")
public class PhoneController {

    private final PhoneService phoneService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPhone(
            @RequestPart("phone") String phoneJson,
            @RequestPart("image") MultipartFile image
    ) throws Exception {

        // JSON 문자열 → DTO 변환
        PhoneCreateRequest request =
                objectMapper.readValue(phoneJson, PhoneCreateRequest.class);

        phoneService.createPhone(request, image);
    }
}
