package com.wms.flowerwms.warehouse.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseSearchCond {
    private String keyword;
    private String code;
    private String name;
    private String address;
}
