package com.wms.flowerwms.member.query.service;

import com.wms.flowerwms.global.paging.PageResponse;
import com.wms.flowerwms.member.domain.Member;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.query.dto.MemberListRow;
import com.wms.flowerwms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberQueryService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public PageResponse<MemberListRow> listAll(MemberStatus status, Integer page, Integer size) {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (size == null || size < 1 || size > 100) ? 10 : size;
        return new PageResponse<>(memberRepository.searchMembers(status, PageRequest.of(p - 1, s)));
    }

    // 전체 회원 목록
    @Transactional(readOnly = true)
    public List<Member> listAll() {
        return memberRepository.findAll();
    }

    // PENDING 회원 목록 (승인 대기)
    @Transactional(readOnly = true)
    public List<Member> listPending() {
        return memberRepository.findAllByStatus(MemberStatus.PENDING);
    }
}