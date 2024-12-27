package com.stock.exchange.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;
    private String name;
}
