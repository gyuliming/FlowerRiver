package com.wms.flowerwms.warehouse.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.section.repository.SectionRepository;
import com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView;
import com.wms.flowerwms.warehouse.query.dto.WarehouseListRow;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseQueryService {

    private final WarehouseRepository warehouseRepository;
    private final SectionRepository sectionRepository;

    public PageResponse<WarehouseListRow> list(String keyword, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        Page<WarehouseListRow> result = warehouseRepository.searchWarehouses(
                keyword,
                PageRequest.of(p - 1, s)
        );

        return new PageResponse<>(result);
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