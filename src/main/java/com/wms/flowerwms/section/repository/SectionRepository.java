package com.wms.flowerwms.section.repository;

import com.wms.flowerwms.section.domain.Section;
import com.wms.flowerwms.warehouse.query.dto.SectionSummaryRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    // 섹션 목록 조회
    List<Section> findByWarehouseId(Long warehouseId);

    // 섹션 상세 조회
    @Query("""
        select new com.wms.flowerwms.warehouse.query.dto.SectionSummaryRow(
            s.id, s.code, s.type,
            count(p.id),
            coalesce(sum(p.maxBoxQty), 0),
            coalesce(sum(p.usedBoxQty), 0)
        )
        from Section s
        left join Pallet p on p.section = s
        where s.warehouse.id = :warehouseId
        group by s.id, s.code, s.type
        order by s.type asc
        """)
    List<SectionSummaryRow> findSectionSummaries(@Param("warehouseId") Long warehouseId);
}