package com.wms.flowerwms.member.service;

import com.wms.flowerwms.global.jwt.JwtTokenProvider;
import com.wms.flowerwms.member.domain.Member;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.dto.LoginRequest;
import com.wms.flowerwms.member.dto.LoginResponse;
import com.wms.flowerwms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest req) {
        Member member = memberRepository.findByLoginId(req.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (member.getStatus() == (MemberStatus.PENDING) || member.getStatus() == MemberStatus.REJECTED) {
            throw new IllegalArgumentException("승인되지 않은 계정입니다. 관리자에게 문의하세요.");
        } else if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new IllegalArgumentException("임시 비활성화된 계정입니다. 관리자에게 문의하세요.");
        }

        Long warehouseId = member.getWarehouse() != null ? member.getWarehouse().getId() : null;

        String token = jwtTokenProvider.createToken(
                member.getId(),
                member.getLoginId(),
                member.getRole(),
                warehouseId
        );

        return new LoginResponse(
                token,
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getRole(),
                warehouseId
        );
    }
}