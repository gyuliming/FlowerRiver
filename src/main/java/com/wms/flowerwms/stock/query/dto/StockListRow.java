package com.wms.flowerwms.stock.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StockListRow {
    private Long id;
    private String warehouseName;
    private String sectionCode;
    private String palletCode;
    private String productName;
    private int boxQty;
    private LocalDateTime inboundAt;
}