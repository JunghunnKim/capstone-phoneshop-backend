package com.shop.phoneshop.repository;

import com.shop.phoneshop.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPhoneIdOrderByIdDesc(Long phoneId);
}
