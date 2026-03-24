package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.model.Review;
import com.shop.phoneshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserAndPhone(User user, Phone phone);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.phone.id = :phoneId")
    Double findAverageRatingByPhoneId(@Param("phoneId") Long phoneId);

    List<Review> findByPhoneOrderByCreatedAtDesc(Phone phone);

    void deleteByUser(User user);
}
