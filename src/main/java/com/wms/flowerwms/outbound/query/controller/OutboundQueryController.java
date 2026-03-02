package com.wms.flowerwms.outbound.query.controller;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.outbound.query.dto.OutboundListRow;
import com.wms.flowerwms.outbound.query.service.OutboundQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/outbound")
public class OutboundQueryController {

    private final OutboundQueryService outboundQueryService;

    @GetMapping
    public ApiResponse<PageResponse<OutboundListRow>> list(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(outboundQueryService.list(warehouseId, page, size));
    }
}