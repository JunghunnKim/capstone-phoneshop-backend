package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.PhoneCreateRequest;
import com.shop.phoneshop.dto.PhoneDetailResponse;
import com.shop.phoneshop.dto.PhoneResponse;
import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.repository.FavoriteRepository;
import com.shop.phoneshop.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final FavoriteRepository favoriteRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    /// 핸드폰 등록
    public void createPhone(PhoneCreateRequest request, MultipartFile image) throws IOException {

        String uploadDir = System.getProperty("user.dir") + "/uploads/phones/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = image.getOriginalFilename();
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String savedFilename = UUID.randomUUID() + extension;
        File saveFile = new File(uploadDir + savedFilename);
        image.transferTo(saveFile);

        // DB에는 경로만 저장
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
                        .imageUrl(baseUrl + phone.getImageUrl())
                        .favoriteCount(favoriteRepository.countByPhone(phone))
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
                .imageUrl(baseUrl + phone.getImageUrl())
                .display(phone.getDisplay())
                .processor(phone.getProcessor())
                .ram(phone.getRam())
                .storage(phone.getStorage())
                .battery(phone.getBattery())
                .camera(phone.getCamera())
                .build();
    }

    /// 특정 핸드폰 삭제
    public void deletePhone(Long id) {

        Phone phone = phoneRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 핸드폰이 존재하지 않습니다."));

        deleteImageFile(phone.getImageUrl());

        phoneRepository.delete(phone);
    }

    private void deleteImageFile(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) return;

        // imageUrl: /images/phones/abc.jpg
        String filename = imageUrl.replace("/images/phones/", "");
        String filePath = System.getProperty("user.dir")
                + "/uploads/phones/"
                + filename;

        File file = new File(filePath);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("이미지 파일 삭제 실패: " + filePath);
            }
        }
    }
}