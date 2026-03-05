package com.wms.flowerwms.inbound.repository;

import com.wms.flowerwms.inbound.domain.Inbound;
import com.wms.flowerwms.inbound.query.dto.InboundListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface InboundRepository extends JpaRepository<Inbound, Long> {
    long countByCodeStartingWith(String prefix);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
    select new com.wms.flowerwms.inbound.query.dto.InboundListRow(
        i.id, i.code, w.name, s.code, p.code, pr.name, i.boxQty, i.createdAt
    )
    from Inbound i
    join i.warehouse w
    join i.section s
    join i.pallet p
    join i.product pr
    where (:warehouseId is null or w.id = :warehouseId)
    order by i.createdAt desc
    """)
    Page<InboundListRow> searchInbounds(
            @Param("warehouseId") Long warehouseId,
            Pageable pageable
    );

    @Query("select count(i) from Inbound i where i.createdAt between :start and :end and (:warehouseId is null or i.warehouse.id = :warehouseId)")
    long countByWarehouseAndCreatedAtBetween(@Param("warehouseId") Long warehouseId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select coalesce(sum(i.boxQty), 0) from Inbound i where i.createdAt between :start and :end and (:warehouseId is null or i.warehouse.id = :warehouseId)")
    long sumBoxQtyByWarehouseAndCreatedAtBetween(@Param("warehouseId") Long warehouseId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}