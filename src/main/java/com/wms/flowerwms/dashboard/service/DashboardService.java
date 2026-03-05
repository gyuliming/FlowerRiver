package com.wms.flowerwms.dashboard.service;

import com.wms.flowerwms.dashboard.dto.DashboardProductStockRow;
import com.wms.flowerwms.dashboard.dto.DashboardSummaryResponse;
import com.wms.flowerwms.dashboard.dto.DashboardWarehouseUsageRow;
import com.wms.flowerwms.global.security.SecurityUtil;
import com.wms.flowerwms.inbound.query.dto.InboundListRow;
import com.wms.flowerwms.inbound.repository.InboundRepository;
import com.wms.flowerwms.outbound.query.dto.OutboundListRow;
import com.wms.flowerwms.outbound.repository.OutboundRepository;
import com.wms.flowerwms.pallet.repository.PalletRepository;
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
    private final PalletRepository palletRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        Long warehouseId = SecurityUtil.isAdmin() ? null : SecurityUtil.getCurrentWarehouseId();

        long totalWarehouses = SecurityUtil.isAdmin()
                ? warehouseRepository.countByStatus(WarehouseStatus.NORMAL)
                : 1L;
        long totalStockBox = stockRepository.sumTotalStockBoxByWarehouse(warehouseId);
        long todayInboundCount = inboundRepository.countByWarehouseAndCreatedAtBetween(warehouseId, startOfDay, endOfDay);
        long todayInboundBox = inboundRepository.sumBoxQtyByWarehouseAndCreatedAtBetween(warehouseId, startOfDay, endOfDay);
        long todayOutboundCount = outboundRepository.countByWarehouseAndCreatedAtBetween(warehouseId, startOfDay, endOfDay);
        long todayOutboundBox = outboundRepository.sumBoxQtyByWarehouseAndCreatedAtBetween(warehouseId, startOfDay, endOfDay);

        return new DashboardSummaryResponse(
                totalWarehouses, totalStockBox,
                todayInboundCount, todayInboundBox,
                todayOutboundCount, todayOutboundBox
        );
    }

    @Transactional(readOnly = true)
    public List<DashboardWarehouseUsageRow> getWarehouseUsage() {
        Long warehouseId = SecurityUtil.isAdmin() ? null : SecurityUtil.getCurrentWarehouseId();
        return warehouseRepository.findWarehouseUsage(warehouseId);
    }

    @Transactional(readOnly = true)
    public List<InboundListRow> getRecentInbound() {
        Long warehouseId = SecurityUtil.isAdmin() ? null : SecurityUtil.getCurrentWarehouseId();
        return inboundRepository.searchInbounds(warehouseId, PageRequest.of(0, 5)).getContent();
    }

    @Transactional(readOnly = true)
    public List<OutboundListRow> getRecentOutbound() {
        Long warehouseId = SecurityUtil.isAdmin() ? null : SecurityUtil.getCurrentWarehouseId();
        return outboundRepository.searchOutbounds(warehouseId, PageRequest.of(0, 5)).getContent();
    }

    @Transactional(readOnly = true)
    public List<DashboardProductStockRow> getProductStock() {
        Long warehouseId = SecurityUtil.getCurrentWarehouseId();
        return stockRepository.findProductStockByWarehouse(warehouseId);
    }
}