package com.wms.flowerwms.member.domain;

public enum MemberStatus {
    PENDING,   // 대기
    ACTIVE,    // 승인
    REJECTED,   // 거절
    INACTIVE // 임시 비활성(창고 폐쇄시)
}