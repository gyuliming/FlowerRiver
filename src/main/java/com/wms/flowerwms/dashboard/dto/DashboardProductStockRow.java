package com.wms.flowerwms.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardProductStockRow {
    private String productName;
    private long boxQty;
}