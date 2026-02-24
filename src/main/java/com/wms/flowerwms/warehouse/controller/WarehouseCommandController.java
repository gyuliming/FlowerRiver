package com.wms.flowerwms.warehouse.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.warehouse.dto.WarehouseCreateRequest;
import com.wms.flowerwms.warehouse.service.WarehouseCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouses")
public class WarehouseCommandController {

    private final WarehouseCommandService warehouseCommandService;

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid WarehouseCreateRequest req) {
        Long id = warehouseCommandService.createWarehouse(req);
        return ApiResponse.success("창고 생성 완료", id);
    }
}