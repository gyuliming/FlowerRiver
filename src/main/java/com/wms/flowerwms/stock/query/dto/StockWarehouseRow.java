package com.wms.flowerwms.stock.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockWarehouseRow {
    private Long id;
    private String name;
    private Long stockQty;
}
