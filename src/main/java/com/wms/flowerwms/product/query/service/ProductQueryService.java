package com.wms.flowerwms.product.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.product.domain.FlowerType;
import com.wms.flowerwms.product.query.dto.ProductListRow;
import com.wms.flowerwms.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductListRow> list(String keyword, FlowerType type, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        Page<ProductListRow> result = productRepository.searchProducts(
                keyword, type, PageRequest.of(p - 1, s)
        );

        return new PageResponse<>(result);
    }
}