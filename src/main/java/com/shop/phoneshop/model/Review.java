package com.shop.phoneshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "phone_id"})
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /// 대상 핸드폰
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_id", nullable = false)
    private Phone phone;

    /// 평점 (1~5)
    private int rating;

    /// 리뷰 내용
    @Column(length = 500)
    private String content;

    public Review(User user, Phone phone, int rating, String content) {
        this.user = user;
        this.phone = phone;
        this.rating = rating;
        this.content = content;
    }
}
