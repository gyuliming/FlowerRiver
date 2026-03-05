package com.wms.flowerwms.member.dto;

import com.wms.flowerwms.member.domain.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long memberId;
    private String loginId;
    private String name;
    private MemberRole role;
    private Long warehouseId;
}