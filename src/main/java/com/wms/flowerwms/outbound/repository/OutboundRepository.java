package com.wms.flowerwms.outbound.repository;

import com.wms.flowerwms.outbound.domain.Outbound;
import com.wms.flowerwms.outbound.query.dto.OutboundListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OutboundRepository extends JpaRepository<Outbound, Long> {
    long countByCodeStartingWith(String prefix);

    @Query("""
    select new com.wms.flowerwms.outbound.query.dto.OutboundListRow(
        o.id, o.code, w.name, s.code, p.code, pr.name, o.boxQty, o.createdAt
    )
    from Outbound o
    join o.warehouse w
    join o.section s
    join o.pallet p
    join o.product pr
    where (:warehouseId is null or w.id = :warehouseId)
    order by o.createdAt desc
    """)
    Page<OutboundListRow> searchOutbounds(
            @Param("warehouseId") Long warehouseId,
            Pageable pageable
    );
}