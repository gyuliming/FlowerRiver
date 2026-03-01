package com.wms.flowerwms.product.dto;

import com.wms.flowerwms.product.domain.FlowerType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductCreateRequest {
    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @NotNull(message = "상품 종류를 선택해주세요.")
    private FlowerType type;

    @Min(value = 1, message = "박스당 가격은 1 이상이어야 합니다.")
    private int pricePerBox;

    @Min(value = 1, message = "박스당 수량은 1 이상이어야 합니다.")
    private int qtyPerBox;

    @NotBlank(message = "단위를 입력해주세요.")
    private String unit;

    private String imageUrl;
}