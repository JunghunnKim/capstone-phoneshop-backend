package com.shop.phoneshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawRequest {

    private String email;
    private String name;
    private String password;
}
