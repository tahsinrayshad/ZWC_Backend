package com.zwc.zwcbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
}
// This class is used to encapsulate the response sent back to the client after a successful authentication.