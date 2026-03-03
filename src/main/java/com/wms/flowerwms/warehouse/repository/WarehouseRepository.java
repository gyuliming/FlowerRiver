package com.wms.flowerwms.warehouse.repository;

import com.wms.flowerwms.dashboard.dto.DashboardWarehouseUsageRow;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.domain.WarehouseStatus;
import com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView;
import com.wms.flowerwms.warehouse.query.dto.WarehouseListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    // 코드로 창고 찾기(중복 확인용)
    Optional<Warehouse> findByCode(String code);

    // 이름으로 창고 찾기(중복 확인용)
    Optional<Warehouse> findByName(String name);

    // 주소로 창고 찾기(중복 확인용)
    Optional<Warehouse> findByAddress(String address);

    long countByStatus(WarehouseStatus status);

    // 창고 목록 페이징 검색
    @Query("""
    select new com.wms.flowerwms.warehouse.query.dto.WarehouseListRow(
        w.id, w.code, w.name, w.address, w.status,
        count(distinct s.id),
        count(distinct p.id),
        coalesce(sum(p.maxBoxQty), 0),
        coalesce(sum(p.usedBoxQty), 0)
    )
    from Warehouse w
    left join Section s on s.warehouse = w
    left join Pallet p on p.section = s
    where (:keyword is null or :keyword = ''
           or w.code like concat('%', :keyword, '%')
           or w.name like concat('%', :keyword, '%')
           or w.address like concat('%', :keyword, '%'))
               and(:code is null or :code = '' or w.code like concat('%', :code, '%'))
               and (:name is null or :name = '' or w.name like concat('%', :name, '%'))
               and (:address is null or :address = '' or w.address like concat('%', :address, '%'))
    group by w.id, w.code, w.name, w.address, w.status
    """)
    Page<WarehouseListRow> searchWarehouses(
            @Param("keyword") String keyword,
            @Param("code") String code,
            @Param("name") String name,
            @Param("address") String address,
            Pageable pageable);

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

    @Query("""
    select new com.wms.flowerwms.dashboard.dto.DashboardWarehouseUsageRow(
        w.name,
        coalesce(sum(s.boxQty), 0)
    )
    from Warehouse w
    left join Stock s on s.warehouse = w and s.boxQty > 0
    where w.status = 'NORMAL'
    group by w.id, w.name
    order by w.name asc
    """)
    List<DashboardWarehouseUsageRow> findWarehouseUsage();
}
