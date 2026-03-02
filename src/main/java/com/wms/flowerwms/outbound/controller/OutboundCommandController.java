package com.wms.flowerwms.outbound.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.outbound.dto.OutboundCreateRequest;
import com.wms.flowerwms.outbound.service.OutboundCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/outbound")
public class OutboundCommandController {

    private final OutboundCommandService outboundCommandService;

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid OutboundCreateRequest req) {
        Long id = outboundCommandService.createOutbound(req);
        return ApiResponse.success("출고 등록 완료", id);
    }
}