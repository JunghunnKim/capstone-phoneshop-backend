package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.PhoneCreateRequest;
import com.shop.phoneshop.model.Phone;
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

    public void createPhone(PhoneCreateRequest request, MultipartFile image) throws IOException {

        /// 업로드 경로 (프로젝트 루트 기준)
        String uploadDir = System.getProperty("user.dir") + "/uploads/phones/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        /// 파일명 생성
        String savedFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();

        /// 파일 저장
        File saveFile = new File(uploadDir + savedFilename);
        image.transferTo(saveFile);

        /// DB 저장
        Phone phone = new Phone(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                "/images/phones/" + savedFilename
        );

        phoneRepository.save(phone);
    }
}
