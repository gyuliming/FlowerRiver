package com.wms.flowerwms.warehouse.query.dto;

import com.wms.flowerwms.warehouse.domain.WarehouseStatus;
import lombok.Getter;

@Getter
public class WarehouseListRow {
    private final Long id;
    private final String code;
    private final String name;
    private final String address;
    private final WarehouseStatus status;

    private final long sectionCount;
    private final long palletCount;
    private final long totalCapacityBox;
    private final long usedBox;

    public WarehouseListRow(Long id, String code, String name, String address, WarehouseStatus status,
                            long sectionCount, long palletCount, long totalCapacityBox, long usedBox) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.status = status;
        this.sectionCount = sectionCount;
        this.palletCount = palletCount;
        this.totalCapacityBox = totalCapacityBox;
        this.usedBox = usedBox;
    }
}