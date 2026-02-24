package com.wms.flowerwms.pallet.service;

import com.wms.flowerwms.pallet.dto.PalletRow;
import com.wms.flowerwms.pallet.repository.PalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PalletQueryService {

    private final PalletRepository palletRepository;

    public List<PalletRow> listBySection(Long sectionId) {
        return palletRepository.findBySectionIdOrderByIdAsc(sectionId)
                .stream()
                .map(p -> new PalletRow(p.getId(), p.getCode(), p.getMaxBoxQty(), p.getUsedBoxQty()))
                .toList();
    }
}