package com.wms.flowerwms.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockPalletResponse {
    private Long palletId;
    private String palletCode;
    private int stockQty;
}