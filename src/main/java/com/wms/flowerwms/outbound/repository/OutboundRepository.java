package com.wms.flowerwms.outbound.repository;

import com.wms.flowerwms.outbound.domain.Outbound;
import com.wms.flowerwms.outbound.query.dto.OutboundListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OutboundRepository extends JpaRepository<Outbound, Long> {
    long countByCodeStartingWith(String prefix);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

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

    @Query("select count(o) from Outbound o where o.createdAt between :start and :end and (:warehouseId is null or o.warehouse.id = :warehouseId)")
    long countByWarehouseAndCreatedAtBetween(@Param("warehouseId") Long warehouseId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select coalesce(sum(o.boxQty), 0) from Outbound o where o.createdAt between :start and :end and (:warehouseId is null or o.warehouse.id = :warehouseId)")
    long sumBoxQtyByWarehouseAndCreatedAtBetween(@Param("warehouseId") Long warehouseId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}