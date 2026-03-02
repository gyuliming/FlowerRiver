package com.wms.flowerwms.outbound.domain;

import com.wms.flowerwms.outbound.repository.OutboundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class OutboundCodeGenerator {

    private final OutboundRepository outboundRepository;

    // 출고 코드 자동 생성 로직 - 예) OB-20260302-0001
    public String nextCode() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "OB-" + today + "-";

        long count = outboundRepository.countByCodeStartingWith(prefix);
        return prefix + String.format("%04d", count + 1);
    }
}