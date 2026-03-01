package com.wms.flowerwms.product.domain;

import com.wms.flowerwms.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCodeGenerator {

    private final ProductRepository productRepository;

    // 상품 코드 자동 생성 로직 - 예) PRD-000001
    public String nextCode() {
        long count = productRepository.count();
        return String.format("PRD-%06d", count + 1);
    }
}