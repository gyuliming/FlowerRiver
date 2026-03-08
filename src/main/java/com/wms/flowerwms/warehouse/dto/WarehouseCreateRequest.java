package com.wms.flowerwms.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseCreateRequest {
    @NotBlank(message = "창고 이름은 필수입니다.")
    @Size(max = 100, message = "창고 이름은 100자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
    private String address;
}

