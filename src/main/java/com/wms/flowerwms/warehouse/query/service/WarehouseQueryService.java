package com.wms.flowerwms.warehouse.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.section.repository.SectionRepository;
import com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView;
import com.wms.flowerwms.warehouse.query.dto.WarehouseListRow;
import com.wms.flowerwms.warehouse.query.dto.WarehouseSearchCond;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseQueryService {

    private final WarehouseRepository warehouseRepository;
    private final SectionRepository sectionRepository;

    @Transactional(readOnly = true)
    public PageResponse<WarehouseListRow> list(WarehouseSearchCond cond, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        log.info("=== After JPQL 쿼리 실행 ===");
        Page<WarehouseListRow> result = warehouseRepository.searchWarehouses(
                cond.getKeyword(),
                cond.getCode(),
                cond.getName(),
                cond.getAddress(),
                PageRequest.of(p - 1, s)
        );
        log.info("=== 쿼리 완료 ===");

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