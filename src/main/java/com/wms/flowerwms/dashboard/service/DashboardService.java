package com.wms.flowerwms.dashboard.service;

import com.wms.flowerwms.dashboard.dto.DashboardSummaryResponse;
import com.wms.flowerwms.dashboard.dto.DashboardWarehouseUsageRow;
import com.wms.flowerwms.inbound.query.dto.InboundListRow;
import com.wms.flowerwms.inbound.repository.InboundRepository;
import com.wms.flowerwms.outbound.query.dto.OutboundListRow;
import com.wms.flowerwms.outbound.repository.OutboundRepository;
import com.wms.flowerwms.stock.repository.StockRepository;
import com.wms.flowerwms.warehouse.domain.WarehouseStatus;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final WarehouseRepository warehouseRepository;
    private final StockRepository stockRepository;
    private final InboundRepository inboundRepository;
    private final OutboundRepository outboundRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long totalWarehouses = warehouseRepository.countByStatus(WarehouseStatus.NORMAL);
        long totalStockBox = stockRepository.sumTotalStockBox();
        long todayInboundCount = inboundRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long todayInboundBox = inboundRepository.sumBoxQtyByCreatedAtBetween(startOfDay, endOfDay);
        long todayOutboundCount = outboundRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        long todayOutboundBox = outboundRepository.sumBoxQtyByCreatedAtBetween(startOfDay, endOfDay);

        return new DashboardSummaryResponse(
                totalWarehouses, totalStockBox,
                todayInboundCount, todayInboundBox,
                todayOutboundCount, todayOutboundBox
        );
    }

    @Transactional(readOnly = true)
    public List<DashboardWarehouseUsageRow> getWarehouseUsage() {
        return warehouseRepository.findWarehouseUsage();
    }

    @Transactional(readOnly = true)
    public List<InboundListRow> getRecentInbound() {
        return inboundRepository.searchInbounds(null, PageRequest.of(0, 5)).getContent();
    }

    @Transactional(readOnly = true)
    public List<OutboundListRow> getRecentOutbound() {
        return outboundRepository.searchOutbounds(null, PageRequest.of(0, 5)).getContent();
    }
}