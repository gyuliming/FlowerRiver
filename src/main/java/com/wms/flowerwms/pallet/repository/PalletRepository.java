package com.wms.flowerwms.pallet.repository;

import com.wms.flowerwms.pallet.domain.Pallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PalletRepository extends JpaRepository<Pallet, Long> {
    Optional<Pallet> findByCode(String code);

    // 구역별 팔레트 목록 조회 (정렬은 id 기준)
    List<Pallet> findBySectionIdOrderByIdAsc(Long sectionId);
}