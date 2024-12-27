package com.stock.exchange.model;

import lombok.Data;

@Data
public class RegisterUser {
    private String emailId;
    private String password;
    private String name;
}
