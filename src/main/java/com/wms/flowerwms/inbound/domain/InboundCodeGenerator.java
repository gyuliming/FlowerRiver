package com.wms.flowerwms.inbound.domain;

import com.wms.flowerwms.inbound.repository.InboundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class InboundCodeGenerator {

    private final InboundRepository inboundRepository;

    // 입고 코드 자동 생성 로직 - 예) IB-20260302-0001
    public String nextCode() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "IB-" + today + "-";

        long count = inboundRepository.countByCodeStartingWith(prefix);
        return prefix + String.format("%04d", count + 1);
    }
}