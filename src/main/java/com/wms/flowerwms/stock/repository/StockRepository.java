package com.wms.flowerwms.stock.repository;

import com.wms.flowerwms.stock.domain.Stock;
import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.product.domain.Product;
import com.wms.flowerwms.stock.query.dto.StockListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductAndPallet(Product product, Pallet pallet);

    // 특정 상품의 재고가 있는 팔레트 목록
    @Query("""
    select s from Stock s
    join fetch s.pallet p
    where s.product.id = :productId
    and s.section.id = :sectionId
    and s.boxQty > 0
    """)
    List<Stock> findByProductIdAndSectionIdWithStock(
            @Param("productId") Long productId,
            @Param("sectionId") Long sectionId
    );

    // FIFO : 선입선출
    @Query("""
    select s from Stock s
    where s.product.id = :productId
    and s.warehouse.id = :warehouseId
    and s.boxQty > 0
    order by s.inboundAt asc
    """)
    List<Stock> findByProductAndWarehouseOrderByInboundAtAsc(
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId
    );

    @Query("""
    select new com.wms.flowerwms.stock.query.dto.StockListRow(
        s.id, w.name, sec.code, p.code, pr.name, s.boxQty, s.inboundAt
    )
    from Stock s
    join s.warehouse w
    join s.section sec
    join s.pallet p
    join s.product pr
    where s.boxQty > 0
    and (:warehouseId is null or w.id = :warehouseId)
    and (:productId is null or pr.id = :productId)
    order by s.inboundAt asc
    """)
    Page<StockListRow> searchStocks(
            @Param("warehouseId") Long warehouseId,
            @Param("productId") Long productId,
            Pageable pageable
    );
}