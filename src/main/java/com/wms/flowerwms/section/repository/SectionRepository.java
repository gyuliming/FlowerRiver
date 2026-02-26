package com.wms.flowerwms.section.repository;

import com.wms.flowerwms.section.domain.Section;
import com.wms.flowerwms.warehouse.query.dto.SectionSummaryRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    // 섹션 목록 조회
    List<Section> findByWarehouse(Long warehouseId);
}