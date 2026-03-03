package com.wms.flowerwms.stock.query.dto;

import com.wms.flowerwms.product.domain.FlowerType;
import com.wms.flowerwms.section.domain.SectionType;
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
    private FlowerType productType;
    private SectionType storageType;
    private int boxQty;
}