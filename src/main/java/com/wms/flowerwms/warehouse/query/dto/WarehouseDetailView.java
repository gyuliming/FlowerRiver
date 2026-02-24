package com.wms.flowerwms.warehouse.query.dto;

import com.wms.flowerwms.warehouse.domain.WarehouseStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class WarehouseDetailView {
    private final Long id;
    private final String code;
    private final String name;
    private final String address;
    private final WarehouseStatus status;

    private final long totalCapacityBox;
    private final long usedBox;

    private List<SectionSummaryRow> sections;

    public WarehouseDetailView(Long id, String code, String name, String address, WarehouseStatus status,
                               long totalCapacityBox, long usedBox) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.status = status;
        this.totalCapacityBox = totalCapacityBox;
        this.usedBox = usedBox;
    }

    public void setSections(List<SectionSummaryRow> sections) {
        this.sections = sections;
    }
}