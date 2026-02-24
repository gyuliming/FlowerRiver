package com.wms.flowerwms.pallet.repository;

import com.wms.flowerwms.pallet.domain.Pallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PalletRepository extends JpaRepository<Pallet, Long> {
    Optional<Pallet> findByCode(String code);
}