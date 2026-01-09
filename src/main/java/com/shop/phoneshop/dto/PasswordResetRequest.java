package com.shop.phoneshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PasswordResetRequest {

    private String email;
    private String name;
}
