package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.CommentCreateRequest;
import com.shop.phoneshop.dto.CommentResponse;
import com.shop.phoneshop.dto.CommentUpdateRequest;
import com.shop.phoneshop.security.JwtTokenProvider;
import com.shop.phoneshop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    /// 댓글 추가
    @PostMapping
    public void addComment(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CommentCreateRequest request
    ) {
        Long userId = extractUserId(authorization);
        commentService.addComment(userId, request);
    }

    /// 댓글 조회
    @GetMapping("/phones/{phoneId}")
    public List<CommentResponse> getCommentsByPhone(@PathVariable Long phoneId) {
        return commentService.getCommentsByPhone(phoneId);
    }

    /// 댓글 수정
    @PutMapping("/{commentId}")
    public void updateComment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        Long userId = extractUserId(authorization);
        commentService.updateComment(commentId, userId, request);
    }

    /// 댓글 삭제
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId
    ) {
        Long userId = extractUserId(authorization);
        commentService.deleteComment(commentId, userId);
    }

    // 공통 메서드
    private Long extractUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getUserId(token);
    }
}
