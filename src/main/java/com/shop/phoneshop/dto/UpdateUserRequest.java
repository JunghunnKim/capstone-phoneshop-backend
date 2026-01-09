package com.shop.phoneshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private String email;   // 식별용
    private String name;
    private String password;
}
