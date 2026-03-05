package com.wms.flowerwms.member.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.member.dto.MemberApproveRequest;
import com.wms.flowerwms.member.dto.MemberCreateRequest;
import com.wms.flowerwms.member.service.MemberCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    // 회원가입 (가입 후 PENDING)
    @PostMapping("/register")
    public ApiResponse<Long> register(@RequestBody @Valid MemberCreateRequest req) {
        Long id = memberCommandService.register(req);
        return ApiResponse.success("회원가입 요청이 완료되었습니다.", id);
    }

    // 승인 (ADMIN)
    @PatchMapping("/{memberId}/approve")
    public ApiResponse<Void> approve(@PathVariable Long memberId, @RequestBody @Valid MemberApproveRequest req) {
        memberCommandService.approve(memberId, req);
        return ApiResponse.success("승인되었습니다.", null);
    }

    // 거절 (ADMIN)
    @PatchMapping("/{memberId}/reject")
    public ApiResponse<Void> reject(@PathVariable Long memberId) {
        memberCommandService.reject(memberId);
        return ApiResponse.success("거절되었습니다.", null);
    }
}