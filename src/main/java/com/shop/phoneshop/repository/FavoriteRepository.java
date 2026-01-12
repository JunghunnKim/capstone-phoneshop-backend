package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Favorite;
import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserAndPhone(User user, Phone phone);

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndPhone(User user, Phone phone);
}
