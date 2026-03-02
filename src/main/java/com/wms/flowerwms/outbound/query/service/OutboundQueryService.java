package com.wms.flowerwms.outbound.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.outbound.query.dto.OutboundListRow;
import com.wms.flowerwms.outbound.repository.OutboundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboundQueryService {

    private final OutboundRepository outboundRepository;

    @Transactional(readOnly = true)
    public PageResponse<OutboundListRow> list(Long warehouseId, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        Page<OutboundListRow> result = outboundRepository.searchOutbounds(
                warehouseId,
                PageRequest.of(p - 1, s)
        );

        return new PageResponse<>(result);
    }
}
