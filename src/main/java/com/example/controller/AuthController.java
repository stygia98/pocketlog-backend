package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.User;
import com.example.dto.ApiResponse;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.dto.SignupRequest;
import com.example.dto.UpdateNicknameRequest;
import com.example.dto.UserResponse;
import com.example.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request.email(), request.password(), request.nickname());
        return ApiResponse.success(null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        return ApiResponse.success(new LoginResponse(token));
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(@AuthenticationPrincipal User user) {
        return ApiResponse.success(new UserResponse(user.getEmail(), user.getNickname()));
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateMe(@AuthenticationPrincipal User user,
                                              @Valid @RequestBody UpdateNicknameRequest request) {
        User updated = authService.updateNickname(user.getId(), request.nickname());
        return ApiResponse.success(new UserResponse(updated.getEmail(), updated.getNickname()));
    }
}
