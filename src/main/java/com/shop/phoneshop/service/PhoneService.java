package com.shop.phoneshop.service;

import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.dto.PhoneCreateRequest;
import com.shop.phoneshop.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;

    private static final String IMAGE_DIR = "uploads/phones/";

    public String createPhone(PhoneCreateRequest request, MultipartFile image) throws IOException {

        // 디렉토리 없으면 생성
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일명 중복 방지
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        File saveFile = new File(IMAGE_DIR + fileName);

        image.transferTo(saveFile);

        Phone phone = new Phone(
                request.getModelName(),
                request.getBrand(),
                request.getPrice(),
                request.getStock(),
                IMAGE_DIR + fileName
        );

        phoneRepository.save(phone);

        return "핸드폰 등록 완료";
    }
}
