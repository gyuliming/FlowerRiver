package com.wms.flowerwms.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardWarehouseUsageRow {
    private String warehouseName;
    private long totalStockBox;
}