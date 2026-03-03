package com.wms.flowerwms.stock.query.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.stock.dto.StockPalletResponse;
import com.wms.flowerwms.stock.query.dto.StockHistoryRow;
import com.wms.flowerwms.stock.query.dto.StockListRow;
import com.wms.flowerwms.stock.query.service.StockQueryService;
import com.wms.flowerwms.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockQueryController {

    private final StockQueryService stockQueryService;
    private final StockRepository stockRepository;

    // 재고 목록
    @GetMapping
    public ApiResponse<PageResponse<StockListRow>> list(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(stockQueryService.listStocks(warehouseId, productId, page, size));
    }

    // 재고 이력
    @GetMapping("/history")
    public ApiResponse<PageResponse<StockHistoryRow>> history(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(stockQueryService.listHistories(warehouseId, productId, page, size));
    }

    // 재고 있는 팔레트 목록 (출고용)
    @GetMapping("/pallets")
    public ApiResponse<List<StockPalletResponse>> getPalletsByProductAndSection(
            @RequestParam Long productId,
            @RequestParam Long sectionId
    ) {
        List<StockPalletResponse> result = stockRepository
                .findByProductIdAndSectionIdWithStock(productId, sectionId)
                .stream()
                .map(s -> new StockPalletResponse(
                        s.getPallet().getId(),
                        s.getPallet().getCode(),
                        s.getBoxQty()
                ))
                .toList();

        return ApiResponse.success(result);
    }
}