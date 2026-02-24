package com.wms.flowerwms.pallet.service;

import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.pallet.repository.PalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PalletCommandService {

    private final PalletRepository palletRepository;

    @Transactional
    public void inbound(Long palletId, int qty) {
        Pallet pallet = palletRepository.findById(palletId)
                .orElseThrow(() -> new IllegalArgumentException("팔레트가 존재하지 않습니다."));
        pallet.increaseUsed(qty);
    }

    @Transactional
    public void outbound(Long palletId, int qty) {
        Pallet pallet = palletRepository.findById(palletId)
                .orElseThrow(() -> new IllegalArgumentException("팔레트가 존재하지 않습니다."));
        pallet.decreaseUsed(qty);
    }
}