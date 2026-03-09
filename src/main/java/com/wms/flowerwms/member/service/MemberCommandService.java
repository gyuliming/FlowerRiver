package com.wms.flowerwms.member.service;

import com.wms.flowerwms.member.domain.Member;
import com.wms.flowerwms.member.domain.MemberRole;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.dto.MemberApproveRequest;
import com.wms.flowerwms.member.dto.MemberCreateRequest;
import com.wms.flowerwms.member.dto.MemberUpdateRequest;
import com.wms.flowerwms.member.dto.PasswordChangeRequest;
import com.wms.flowerwms.member.repository.MemberRepository;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final WarehouseRepository warehouseRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 (창고관리자만)
    @Transactional
    public Long register(MemberCreateRequest req) {
        if (memberRepository.existsByLoginId(req.getLoginId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        Member member = Member.builder()
                .loginId(req.getLoginId())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .phone(req.getPhone())
                .email(req.getEmail())
                .role(MemberRole.MANAGER)
                .build();

        return memberRepository.save(member).getId();
    }

    // 승인
    @Transactional
    public void approve(Long memberId, MemberApproveRequest req) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Warehouse warehouse = warehouseRepository.findById(req.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다."));

        // 해당 창고에 이미 담당자가 있는지 확인
        if (memberRepository.existsByWarehouseAndStatus(warehouse, MemberStatus.ACTIVE)) {
            throw new IllegalArgumentException("해당 창고에 이미 담당자가 있습니다.");
        }

        member.approve(warehouse);
    }

    @Transactional
    public void reject(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        member.reject();
    }

    @Transactional
    public void updateMyInfo(Long memberId, MemberUpdateRequest req) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        member.updateInfo(req.getName(), req.getPhone(), req.getEmail());
    }

    @Transactional
    public void changePassword(Long memberId, PasswordChangeRequest req) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(req.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        member.changePassword(passwordEncoder.encode(req.getNewPassword()));
    }
}