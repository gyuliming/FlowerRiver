package com.wms.flowerwms.outbound.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OutboundListRow {
    private Long id;
    private String code;
    private String warehouseName;
    private String sectionCode;
    private String palletCode;
    private String productName;
    private int boxQty;
    private LocalDateTime createdAt;
}
