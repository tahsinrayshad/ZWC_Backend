package com.zwc.zwcbackend.service;

import com.zwc.zwcbackend.dto.AuthResponse;
import com.zwc.zwcbackend.dto.LoginRequest;
import com.zwc.zwcbackend.dto.SignupRequest;
import com.zwc.zwcbackend.entity.User;
import com.zwc.zwcbackend.repository.UserRepository;
import com.zwc.zwcbackend.util.JwtTokenUtil;
import com.zwc.zwcbackend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        User.Role userRole;
        try {
            userRole = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided. Must be ADMIN or USER.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .contactNumber(request.getContactNumber())
                .role(userRole)
                .build();

        userRepository.save(user);

        String token = jwtTokenUtil.generateToken(new CustomUserDetails(user));

        return new AuthResponse("User registered successfully", token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtTokenUtil.generateToken(new CustomUserDetails(user));

        return new AuthResponse("Login successful", token);
    }
}
