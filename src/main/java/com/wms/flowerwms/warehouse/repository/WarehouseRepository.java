package com.wms.flowerwms.warehouse.repository;

import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView;
import com.wms.flowerwms.warehouse.query.dto.WarehouseListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    // 코드로 창고 하나 찾기
    Optional<Warehouse> findByCode(String code);

    // 창고 목록 페이징 검색
    @Query("""
    select w
    from Warehouse w
    where (:keyword is null or :keyword = ''
           or w.code like concat('%', :keyword, '%')
           or w.name like concat('%', :keyword, '%')
           or w.address like concat('%', :keyword, '%'))
    """)
    Page<Warehouse> searchWarehouses(@Param("keyword") String keyword, Pageable pageable);

    // 창고 상세 조회
    @Query("""
        select new com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView(
            w.id, w.code, w.name, w.address, w.status,
            coalesce(sum(p.maxBoxQty), 0),
            coalesce(sum(p.usedBoxQty), 0)
        )
        from Warehouse w
        left join Section s on s.warehouse = w
        left join Pallet p on p.section = s
        where w.id = :warehouseId
        group by w.id, w.code, w.name, w.address, w.status
        """)
    WarehouseDetailView findDetail(@Param("warehouseId") Long warehouseId);
}