package com.wms.flowerwms.dashboard.controller;

import com.wms.flowerwms.dashboard.dto.DashboardSummaryResponse;
import com.wms.flowerwms.dashboard.dto.DashboardWarehouseUsageRow;
import com.wms.flowerwms.dashboard.service.DashboardService;
import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.inbound.query.dto.InboundListRow;
import com.wms.flowerwms.outbound.query.dto.OutboundListRow;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        return ApiResponse.success(dashboardService.getSummary());
    }

    @GetMapping("/warehouse-usage")
    public ApiResponse<List<DashboardWarehouseUsageRow>> getWarehouseUsage() {
        return ApiResponse.success(dashboardService.getWarehouseUsage());
    }

    @GetMapping("/recent-inbound")
    public ApiResponse<List<InboundListRow>> getRecentInbound() {
        return ApiResponse.success(dashboardService.getRecentInbound());
    }

    @GetMapping("/recent-outbound")
    public ApiResponse<List<OutboundListRow>> getRecentOutbound() {
        return ApiResponse.success(dashboardService.getRecentOutbound());
    }
}