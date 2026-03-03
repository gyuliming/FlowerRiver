package com.wms.flowerwms.stock.repository;

import com.wms.flowerwms.stock.domain.StockHistory;
import com.wms.flowerwms.stock.query.dto.StockHistoryRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    @Query("""
    select new com.wms.flowerwms.stock.query.dto.StockHistoryRow(
        h.id, w.name, pr.name, h.type, h.boxQty, h.createdAt
    )
    from StockHistory h
    join h.warehouse w
    join h.product pr
    where (:warehouseId is null or w.id = :warehouseId)
    and (:productId is null or pr.id = :productId)
    order by h.createdAt desc
    """)
    Page<StockHistoryRow> searchStockHistories(
            @Param("warehouseId") Long warehouseId,
            @Param("productId") Long productId,
            Pageable pageable
    );
}