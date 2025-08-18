package com.zwc.zwcbackend.dto;

import lombok.*;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
