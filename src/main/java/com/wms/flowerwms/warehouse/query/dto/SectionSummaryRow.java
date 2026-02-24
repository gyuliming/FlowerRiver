package com.wms.flowerwms.warehouse.query.dto;

import com.wms.flowerwms.section.domain.SectionType;
import lombok.Getter;

@Getter
public class SectionSummaryRow {
    private final Long sectionId;
    private final String sectionCode;
    private final SectionType type;

    private final long palletCount;
    private final long totalCapacityBox;
    private final long usedBox;

    public SectionSummaryRow(Long sectionId, String sectionCode, SectionType type,
                             long palletCount, long totalCapacityBox, long usedBox) {
        this.sectionId = sectionId;
        this.sectionCode = sectionCode;
        this.type = type;
        this.palletCount = palletCount;
        this.totalCapacityBox = totalCapacityBox;
        this.usedBox = usedBox;
    }
}