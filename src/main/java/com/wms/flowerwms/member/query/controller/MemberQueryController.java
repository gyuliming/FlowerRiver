package com.wms.flowerwms.member.query.controller;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.member.domain.Member;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.query.dto.MemberListRow;
import com.wms.flowerwms.member.query.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberQueryController {
    private final MemberQueryService memberQueryService;

    // 전체 회원 목록
    @GetMapping
    public ApiResponse<PageResponse<MemberListRow>> listAll(
            @RequestParam(required = false) MemberStatus status,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(memberQueryService.listAll(status, page, size));
    }

    // 승인 대기 목록
    @GetMapping("/pending")
    public ApiResponse<List<Member>> listPending() {
        return ApiResponse.success(memberQueryService.listPending());
    }
}