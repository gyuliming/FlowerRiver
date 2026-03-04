package com.wms.flowerwms.member.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.member.dto.LoginRequest;
import com.wms.flowerwms.member.dto.LoginResponse;
import com.wms.flowerwms.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        return ApiResponse.success("로그인 성공", authService.login(req));
    }
}