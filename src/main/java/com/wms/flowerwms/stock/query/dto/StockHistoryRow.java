package com.wms.flowerwms.stock.query.dto;

import com.wms.flowerwms.stock.domain.StockHistoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StockHistoryRow {
    private Long id;
    private String warehouseName;
    private String productName;
    private StockHistoryType type;
    private int boxQty;
    private LocalDateTime createdAt;
}