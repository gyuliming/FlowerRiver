package com.wms.flowerwms.member.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.member.domain.Member;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.query.dto.MemberInfoResponse;
import com.wms.flowerwms.member.query.dto.MemberListRow;
import com.wms.flowerwms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public PageResponse<MemberListRow> listAll(MemberStatus status, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;

        if (status == null) {
            return new PageResponse<>(memberRepository.findAllMembers(PageRequest.of(p - 1, s)));
        }
        return new PageResponse<>(memberRepository.findAllMembersByStatus(status, PageRequest.of(p - 1, s)));
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return new MemberInfoResponse(
                member.getLoginId(),
                member.getName(),
                member.getPhone(),
                member.getEmail(),
                member.getRole(),
                member.getStatus(),
                member.getWarehouse() != null ? member.getWarehouse().getName() : null,
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
