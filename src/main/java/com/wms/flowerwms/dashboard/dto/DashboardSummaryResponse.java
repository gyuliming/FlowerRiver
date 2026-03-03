package com.wms.flowerwms.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardSummaryResponse {
    private long totalWarehouses;
    private long totalStockBox;
    private long todayInboundCount;
    private long todayInboundBox;
    private long todayOutboundCount;
    private long todayOutboundBox;
}