package com.wms.flowerwms.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberApproveRequest {

    @NotNull(message = "창고를 선택해주세요.")
    private Long warehouseId;
}