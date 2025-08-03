package com.zwc.zwcbackend.dto;

import lombok.*;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String address;
    private String contactNumber;
    private String role; // Can be "ADMIN" or "USER"
}
