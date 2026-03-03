package com.wms.flowerwms.global.config;

import com.wms.flowerwms.warehouse.dto.WarehouseCreateRequest;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import com.wms.flowerwms.warehouse.service.WarehouseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final WarehouseCommandService warehouseCommandService;
    private final WarehouseRepository warehouseRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (warehouseRepository.count() > 0) return; // DB에 데이터가 하나라도 있다면 시더코드는 스킵

        for (int i = 1; i <= 100; i++) {
            warehouseCommandService.createWarehouse(
                    new WarehouseCreateRequest(
                            "창고 " + i,
                            "서울시 강남구 " + i + "번길"
                    )
            );
        }
        System.out.println("시더 완료(목데이터): 창고 100개 생성");
    }
}