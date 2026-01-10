package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndPhoneId(Long userId, Long phoneId);
}
