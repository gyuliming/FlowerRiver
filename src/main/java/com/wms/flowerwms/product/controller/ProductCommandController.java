package com.wms.flowerwms.product.controller;

import com.wms.flowerwms.global.response.ApiResponse;
import com.wms.flowerwms.product.dto.ProductCreateRequest;
import com.wms.flowerwms.product.service.ProductCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductCommandController {

    private final ProductCommandService productCommandService;

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid ProductCreateRequest req) {
        Long id = productCommandService.createProduct(req);
        return ApiResponse.success("상품 등록 완료", id);
    }
}