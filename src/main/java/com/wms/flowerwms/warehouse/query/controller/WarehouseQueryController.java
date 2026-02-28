package com.wms.flowerwms.warehouse.query.controller;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.warehouse.query.dto.WarehouseDetailView;
import com.wms.flowerwms.warehouse.query.dto.WarehouseListRow;
import com.wms.flowerwms.warehouse.query.dto.WarehouseSearchCond;
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
            WarehouseSearchCond cond,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(warehouseQueryService.list(cond, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<WarehouseDetailView> detail(@PathVariable Long id) {
        return ApiResponse.success(warehouseQueryService.detail(id));
    }
}