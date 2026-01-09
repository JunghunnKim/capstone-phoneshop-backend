package com.shop.phoneshop.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    /// 비밀번호 변경메서드
    public void changePassword(String password) {
        this.password = password;
    }

    /// 회원 정보 변경 메서드 (email은 변경 불가)
    public void updateInfo(String name, String password) {
        this.name = name;
        this.password = password;
    }
}



