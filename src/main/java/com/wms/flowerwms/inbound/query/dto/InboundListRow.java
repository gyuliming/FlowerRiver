package com.wms.flowerwms.inbound.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InboundListRow {
    private Long id;
    private String code;
    private String warehouseName;
    private String sectionCode;
    private String palletCode;
    private int boxQty;
    private LocalDateTime createdAt;
}