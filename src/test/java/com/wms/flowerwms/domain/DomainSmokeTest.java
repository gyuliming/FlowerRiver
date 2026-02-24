package com.wms.flowerwms.domain;

import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.pallet.repository.PalletRepository;
import com.wms.flowerwms.section.domain.Section;
import com.wms.flowerwms.section.domain.SectionType;
import com.wms.flowerwms.section.repository.SectionRepository;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DomainSmokeTest {

    @Resource WarehouseRepository warehouseRepository;
    @Resource SectionRepository sectionRepository;
    @Resource PalletRepository palletRepository;

    @Test
    @Transactional
    void codeTest() {
        Warehouse wh = warehouseRepository.save(
                Warehouse.builder()
                        .code("WH-001")
                        .address("서울 강남구")
                        .build()
        );

        Section cold = sectionRepository.save(
                Section.builder()
                        .code("SEC-COLD-001")
                        .type(SectionType.COLD)
                        .warehouse(wh)
                        .build()
        );

        Pallet p1 = palletRepository.save(
                Pallet.builder()
                        .code("PLT-000001")
                        .maxBoxQty(100)
                        .usedBoxQty(0)
                        .section(cold)
                        .build()
        );

        p1.increaseUsed(10);
        assertThat(p1.getUsedBoxQty()).isEqualTo(10);
        assertThat(p1.remainingBoxQty()).isEqualTo(90);
    }
}