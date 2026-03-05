package com.wms.flowerwms.inbound.query.service;

import com.wms.flowerwms.inbound.query.dto.InboundListRow;
import com.wms.flowerwms.inbound.repository.InboundRepository;
import com.wms.flowerwms.global.paging.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.flowerwms.global.security.SecurityUtil;

@Service
@RequiredArgsConstructor
public class InboundQueryService {

    private final InboundRepository inboundRepository;

    @Transactional(readOnly = true)
    public PageResponse<InboundListRow> list(Long warehouseId, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        // MANAGER 는 자기 창고만
        Long filterWarehouseId = SecurityUtil.isAdmin() ? warehouseId : SecurityUtil.getCurrentWarehouseId();

        Page<InboundListRow> result = inboundRepository.searchInbounds(
                filterWarehouseId,
                PageRequest.of(p - 1, s)
        );

        return new PageResponse<>(result);
    }
}