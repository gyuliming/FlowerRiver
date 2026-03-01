package com.wms.flowerwms.inbound.repository;

import com.wms.flowerwms.inbound.domain.Inbound;
import com.wms.flowerwms.inbound.query.dto.InboundListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InboundRepository extends JpaRepository<Inbound, Long> {
    long countByCodeStartingWith(String prefix);

    @Query("""
        select new com.wms.flowerwms.inbound.query.dto.InboundListRow(
            i.id, i.code, w.name, s.code, p.code, i.boxQty, i.createdAt
        )
        from Inbound i
        join i.warehouse w
        join i.section s
        join i.pallet p
        where (:warehouseId is null or w.id = :warehouseId)
        order by i.createdAt desc
        """)
    Page<InboundListRow> searchInbounds(
            @Param("warehouseId") Long warehouseId,
            Pageable pageable
    );
}