package com.wms.flowerwms.warehouse.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.warehouse.dto.WarehouseCreateRequest;
import com.wms.flowerwms.warehouse.dto.WarehouseUpdateRequest;
import com.wms.flowerwms.warehouse.service.WarehouseCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouses")
public class WarehouseCommandController {

    private final WarehouseCommandService warehouseCommandService;

    // 창고 등록
    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid WarehouseCreateRequest req) {
        Long id = warehouseCommandService.createWarehouse(req);
        return ApiResponse.success("창고 등록 완료", id);
    }

    // 창고 수정
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody WarehouseUpdateRequest req) {
        warehouseCommandService.updateWarehouse(id, req);
        return ApiResponse.successMessage("창고 수정 완료");
    }

    // 창고 폐쇄
    @PatchMapping("/{id}/close")
    public ApiResponse<Void> close(@PathVariable Long id) {
        warehouseCommandService.closeWarehouse(id);
        return ApiResponse.successMessage("창고 폐쇄 완료");
    }
}
