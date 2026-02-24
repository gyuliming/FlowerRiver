package com.wms.flowerwms.warehouse.query.controller;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView;
import com.wms.flowerwms.warehouse.query.dto.WarehouseListRow;
import com.wms.flowerwms.warehouse.query.service.WarehouseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouses")
public class WarehouseQueryController {

    private final WarehouseQueryService warehouseQueryService;

    @GetMapping
    public ApiResponse<PageResponse<WarehouseListRow>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(warehouseQueryService.list(keyword, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<WarehouseDetailView> detail(@PathVariable Long id) {
        return ApiResponse.success(warehouseQueryService.detail(id));
    }
}