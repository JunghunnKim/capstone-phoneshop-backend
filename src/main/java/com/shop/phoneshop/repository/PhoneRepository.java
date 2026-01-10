package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
