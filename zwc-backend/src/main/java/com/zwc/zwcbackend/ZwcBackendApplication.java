package com.zwc.zwcbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity // ✅ Enables @PreAuthorize and method-level security
public class ZwcBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZwcBackendApplication.class, args);
    }
}
