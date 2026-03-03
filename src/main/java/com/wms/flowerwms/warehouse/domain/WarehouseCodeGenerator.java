package com.wms.flowerwms.warehouse.domain;

import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseCodeGenerator {

    private final WarehouseRepository warehouseRepository;

    // 창고 코드 자동 생성 로직 - 예) WH-001
    public String nextCode() {
        long count = warehouseRepository.count();
        return String.format("WH-%03d", count + 1);
    }
}
