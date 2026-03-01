package com.wms.flowerwms.inbound.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.inbound.dto.InboundCreateRequest;
import com.wms.flowerwms.inbound.service.InboundCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inbound")
public class InboundCommandController {

    private final InboundCommandService inboundCommandService;

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid InboundCreateRequest req) {
        Long id = inboundCommandService.createInbound(req);
        return ApiResponse.success("입고 등록 완료", id);
    }
}