package com.wms.flowerwms.product.service;

import com.wms.flowerwms.product.domain.Product;
import com.wms.flowerwms.product.domain.ProductCodeGenerator;
import com.wms.flowerwms.product.dto.ProductCreateRequest;
import com.wms.flowerwms.product.repository.ProductRepository;
import com.wms.flowerwms.section.domain.SectionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final ProductCodeGenerator productCodeGenerator;

    @Transactional
    public Long createProduct(ProductCreateRequest req) {
        if (productRepository.existsByName(req.getName())) {
            throw new IllegalArgumentException("이미 존재하는 상품입니다.");
        }

        SectionType storageType = Product.resolveStorageType(req.getType());

        Product product = Product.builder()
                .code(productCodeGenerator.nextCode())
                .name(req.getName())
                .type(req.getType())
                .storageType(storageType)
                .pricePerBox(req.getPricePerBox())
                .qtyPerBox(req.getQtyPerBox())
                .unit(req.getUnit())
                .imageUrl(req.getImageUrl())
                .build();

        return productRepository.save(product).getId();
    }
}