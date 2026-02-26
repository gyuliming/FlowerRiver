package com.wms.flowerwms.warehouse.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.pallet.repository.PalletRepository;
import com.wms.flowerwms.section.domain.Section;
import com.wms.flowerwms.section.repository.SectionRepository;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView;
import com.wms.flowerwms.warehouse.query.dto.WarehouseListRow;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseQueryService {

    private final WarehouseRepository warehouseRepository;
    private final SectionRepository sectionRepository;
    private final PalletRepository palletRepository;

    @Transactional(readOnly = true)
    public PageResponse<WarehouseListRow> list(String keyword, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        Page<Warehouse> warehouses = warehouseRepository.searchWarehouses(keyword, PageRequest.of(p - 1, s));

        List<WarehouseListRow> rows = warehouses.getContent().stream()
                .map(w -> {
                    // 창고마다 섹션 조회
                    List<Section> sections = sectionRepository.findByWarehouseId(w.getId());

                    long palletCount = 0, totalCapacity = 0, usedBox = 0;
                    for (Section sec : sections) {
                        // 섹션마다 팔레트 조회
                        List<Pallet> pallets = palletRepository.findBySectionIdOrderByIdAsc(sec.getId());
                        palletCount += pallets.size();
                        totalCapacity += pallets.stream().mapToLong(Pallet::getMaxBoxQty).sum();
                        usedBox += pallets.stream().mapToLong(Pallet::getUsedBoxQty).sum();
                    }

                    return new WarehouseListRow(
                            w.getId(), w.getCode(), w.getName(), w.getAddress(), w.getStatus(),
                            sections.size(), palletCount, totalCapacity, usedBox
                    );
                }).toList();

        return new PageResponse<>(new PageImpl<>(rows, PageRequest.of(p - 1, s), warehouses.getTotalElements()));
    }

    public WarehouseDetailView detail(Long warehouseId) {
        WarehouseDetailView view = warehouseRepository.findDetail(warehouseId);
        if (view == null) {
            throw new IllegalArgumentException("존재하지 않는 창고입니다.");
        }
        view.setSections(sectionRepository.findSectionSummaries(warehouseId));
        return view;
    }
}