package com.wms.flowerwms.stock.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.stock.query.dto.StockHistoryRow;
import com.wms.flowerwms.stock.query.dto.StockListRow;
import com.wms.flowerwms.stock.repository.StockHistoryRepository;
import com.wms.flowerwms.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockQueryService {

    private final StockRepository stockRepository;
    private final StockHistoryRepository stockHistoryRepository;

    @Transactional(readOnly = true)
    public PageResponse<StockListRow> listStocks(Long warehouseId, Long productId, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        return new PageResponse<>(stockRepository.searchStocks(
                warehouseId, productId, PageRequest.of(p - 1, s)
        ));
    }

    @Transactional(readOnly = true)
    public PageResponse<StockHistoryRow> listHistories(Long warehouseId, Long productId, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        return new PageResponse<>(stockHistoryRepository.searchStockHistories(
                warehouseId, productId, PageRequest.of(p - 1, s)
        ));
    }
}