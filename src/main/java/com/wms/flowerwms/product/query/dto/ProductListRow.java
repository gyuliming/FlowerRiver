package com.wms.flowerwms.product.query.dto;

import com.wms.flowerwms.product.domain.FlowerType;
import com.wms.flowerwms.section.domain.SectionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductListRow {
    private Long id;
    private String code;
    private String name;
    private FlowerType type;
    private SectionType storageType;
    private int pricePerBox;
    private int qtyPerBox;
    private String unit;
    private String imageUrl;
}