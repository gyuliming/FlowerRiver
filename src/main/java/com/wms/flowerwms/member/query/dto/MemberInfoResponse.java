package com.wms.flowerwms.member.query.dto;

import com.wms.flowerwms.member.domain.MemberRole;
import com.wms.flowerwms.member.domain.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberInfoResponse {
    private String loginId;
    private String name;
    private String phone;
    private String email;
    private MemberRole role;
    private MemberStatus status;
    private String warehouseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
