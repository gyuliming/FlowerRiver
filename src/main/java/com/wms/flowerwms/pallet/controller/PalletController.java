package com.wms.flowerwms.pallet.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.pallet.dto.PalletAdjustRequest;
import com.wms.flowerwms.pallet.dto.PalletRow;
import com.wms.flowerwms.pallet.service.PalletCommandService;
import com.wms.flowerwms.pallet.service.PalletQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pallets")
public class PalletController {

    private final PalletCommandService palletCommandService;
    private final PalletQueryService palletQueryService;

    @GetMapping
    public ApiResponse<List<PalletRow>> listBySection(@RequestParam Long sectionId) {
        return ApiResponse.success(palletQueryService.listBySection(sectionId));
    }

    @PostMapping("/{palletId}/inbound")
    public ApiResponse<Void> inbound(@PathVariable Long palletId, @RequestBody @Valid PalletAdjustRequest req) {
        palletCommandService.inbound(palletId, req.getQty());
        return ApiResponse.success("입고 완료", null);
    }

    @PostMapping("/{palletId}/outbound")
    public ApiResponse<Void> outbound(@PathVariable Long palletId, @RequestBody @Valid PalletAdjustRequest req) {
        palletCommandService.outbound(palletId, req.getQty());
        return ApiResponse.success("출고 완료", null);
    }
}