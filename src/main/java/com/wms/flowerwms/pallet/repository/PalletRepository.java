package com.wms.flowerwms.pallet.repository;

import com.wms.flowerwms.pallet.domain.Pallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PalletRepository extends JpaRepository<Pallet, Long> {
    List<Pallet> findBySectionId(Long sectionId);

    // 구역별 팔레트 목록 조회 (정렬은 id 기준)
    List<Pallet> findBySectionIdOrderByIdAsc(Long sectionId);

    @Query("select max(p.id) from Pallet p")
    Optional<Long> findMaxId();

    @Query("select count(p) from Pallet p where p.section.warehouse.id = :warehouseId")
    long countByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("select count(p) from Pallet p where p.section.warehouse.id = :warehouseId and p.usedBoxQty > 0")
    long countUsedByWarehouseId(@Param("warehouseId") Long warehouseId);
}