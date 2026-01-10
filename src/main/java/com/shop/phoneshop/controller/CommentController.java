package com.shop.phoneshop.controller;

import com.shop.phoneshop.dto.CommentCreateRequest;
import com.shop.phoneshop.dto.CommentResponse;
import com.shop.phoneshop.dto.CommentUpdateRequest;
import com.shop.phoneshop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /// 댓글 추가
    @PostMapping
    public void addComment(@RequestBody CommentCreateRequest request) {
        commentService.addComment(request);
    }

    /// 댓글 조회
    @GetMapping("/phones/{phoneId}")
    public List<CommentResponse> getCommentsByPhone(@PathVariable Long phoneId) {
        return commentService.getCommentsByPhone(phoneId);
    }

    /// 댓글 수정
    @PutMapping("/{commentId}")
    public void updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        // 임시 사용자 ID
        Long userId = 1L;

        commentService.updateComment(commentId, userId, request);
    }

    /// 댓글 삭제
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {

        // 임시 사용자 ID
        Long userId = 1L;

        commentService.deleteComment(commentId, userId);
    }

}
