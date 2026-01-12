package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.model.Review;
import com.shop.phoneshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserAndPhone(User user, Phone phone);
}
