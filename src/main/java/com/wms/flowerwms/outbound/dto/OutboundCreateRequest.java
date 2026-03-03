package com.wms.flowerwms.outbound.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OutboundCreateRequest {
    @NotNull(message = "상품을 선택해주세요.")
    private Long productId;

    @NotNull(message = "창고를 선택해주세요.")
    private Long warehouseId;

    @Min(value = 1, message = "출고 수량은 1 이상이어야 합니다.")
    private int boxQty;
}

