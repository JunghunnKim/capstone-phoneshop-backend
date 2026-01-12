package com.shop.phoneshop.service;

import com.shop.phoneshop.dto.CommentCreateRequest;
import com.shop.phoneshop.dto.CommentResponse;
import com.shop.phoneshop.dto.CommentUpdateRequest;
import com.shop.phoneshop.model.Comment;
import com.shop.phoneshop.model.Phone;
import com.shop.phoneshop.model.User;
import com.shop.phoneshop.repository.CommentRepository;
import com.shop.phoneshop.repository.PhoneRepository;
import com.shop.phoneshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    /// 댓글 추가
    public void addComment(Long userId, CommentCreateRequest request) {

        if (request.getPhoneId() == null) {
            throw new IllegalArgumentException("phoneId는 필수입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Phone phone = phoneRepository.findById(request.getPhoneId())
                .orElseThrow(() -> new IllegalArgumentException("폰 없음"));

        Comment comment = Comment.builder()
                .user(user)
                .phone(phone)
                .content(request.getContent())
                .build();

        commentRepository.save(comment);
    }


    /// 댓글 조회
    public List<CommentResponse> getCommentsByPhone(Long phoneId) {

        return commentRepository.findAllByPhoneIdOrderByIdDesc(phoneId)
                .stream()
                .map(comment -> CommentResponse.builder()
                        .commentId(comment.getId())
                        .userId(comment.getUser().getId())
                        .userName(comment.getUser().getName())
                        .content(comment.getContent())
                        .build())
                .toList();
    }

    /// 댓글 수정
    @Transactional
    public void updateComment(Long commentId, Long userId, CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 작성자 검증
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("댓글 수정 권한이 없습니다.");
        }

        comment.updateContent(request.getContent());
    }

    /// 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 작성자 검증
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

}
