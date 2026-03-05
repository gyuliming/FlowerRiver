package com.wms.flowerwms.member.query.controller;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.query.dto.MemberListRow;
import com.wms.flowerwms.member.query.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    @GetMapping
    public ApiResponse<PageResponse<MemberListRow>> listAll(
            @RequestParam(required = false) MemberStatus status,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(memberQueryService.listAll(status, page, size));
    }
}