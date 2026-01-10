package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.PhoneCreateRequest;
import com.shop.phoneshop.service.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/phones")
public class PhoneController {

    private final PhoneService phoneService;

    @PostMapping
    public String createPhone(
            @RequestPart PhoneCreateRequest request,
            @RequestPart MultipartFile image
    ) throws Exception {

        return phoneService.createPhone(request, image);
    }
}
