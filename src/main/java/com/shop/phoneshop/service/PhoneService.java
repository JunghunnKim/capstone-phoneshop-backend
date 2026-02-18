package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.PhoneCreateRequest;
import com.shop.phoneshop.dto.PhoneDetailResponse;
import com.shop.phoneshop.dto.PhoneResponse;
import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;

    /// 핸드폰 등록
    public void createPhone(PhoneCreateRequest request, MultipartFile image) throws IOException {

        // 업로드 경로 (프로젝트 루트 기준)
        String uploadDir = System.getProperty("user.dir") + "/uploads/phones/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일명 생성
        String savedFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();

        // 파일 저장
        File saveFile = new File(uploadDir + savedFilename);
        image.transferTo(saveFile);

        // DB 저장
        Phone phone = new Phone(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                "/images/phones/" + savedFilename,
                request.getDisplay(),
                request.getProcessor(),
                request.getRam(),
                request.getStorage(),
                request.getBattery(),
                request.getCamera()
        );

        phoneRepository.save(phone);
    }

    /// 전체 핸드폰 조회
    public List<PhoneResponse> getAllPhones() {
        return phoneRepository.findAll()
                .stream()
                .map(phone -> PhoneResponse.builder()
                        .id(phone.getId())
                        .name(phone.getName())
                        .brand(phone.getBrand())
                        .price(phone.getPrice())
                        .imageUrl(phone.getImageUrl())
                        .build())
                .toList();
    }

    /// 특정 핸드폰 조회
    public PhoneDetailResponse getPhoneById(Long id) {
        Phone phone = phoneRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 핸드폰이 존재하지 않습니다."));

        return PhoneDetailResponse.builder()
                .id(phone.getId())
                .name(phone.getName())
                .brand(phone.getBrand())
                .price(phone.getPrice())
                .imageUrl(phone.getImageUrl())
                .display(phone.getDisplay())
                .processor(phone.getProcessor())
                .ram(phone.getRam())
                .storage(phone.getStorage())
                .battery(phone.getBattery())
                .camera(phone.getCamera())
                .build();
    }
}
