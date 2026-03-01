package com.wms.flowerwms.inbound.query.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.inbound.query.dto.InboundListRow;
import com.wms.flowerwms.inbound.query.service.InboundQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inbound")
public class InboundQueryController {

    private final InboundQueryService inboundQueryService;

    @GetMapping
    public ApiResponse<PageResponse<InboundListRow>> list(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(inboundQueryService.list(warehouseId, page, size));
    }
}