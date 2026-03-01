package com.wms.flowerwms.product.query.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.product.domain.FlowerType;
import com.wms.flowerwms.product.query.dto.ProductListRow;
import com.wms.flowerwms.product.query.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    @GetMapping
    public ApiResponse<PageResponse<ProductListRow>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) FlowerType type,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return ApiResponse.success(productQueryService.list(keyword, type, page, size));
    }
}
