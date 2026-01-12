package com.shop.phoneshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_id", nullable = false)
    private Phone phone;

    // 댓글 길이 제한하고 싶으면 lenth값 조절
    @Column(nullable = false, length = 500)
    private String content;

    // 댓글 수정 메서드
    public void updateContent(String content) {
        this.content = content;
    }

}
