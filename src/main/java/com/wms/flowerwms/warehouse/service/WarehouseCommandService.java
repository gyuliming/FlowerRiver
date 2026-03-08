package com.wms.flowerwms.warehouse.service;

import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.pallet.repository.PalletRepository;
import com.wms.flowerwms.pallet.service.PalletCodeGenerator;
import com.wms.flowerwms.section.domain.Section;
import com.wms.flowerwms.section.domain.SectionType;
import com.wms.flowerwms.section.repository.SectionRepository;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.domain.WarehouseCodeGenerator;
import com.wms.flowerwms.warehouse.dto.WarehouseCreateRequest;
import com.wms.flowerwms.warehouse.dto.WarehouseUpdateRequest;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarehouseCommandService {

    private static final int PALLETS_PER_SECTION = 20; // 섹션에 20개의 팔레트 존재
    private static final int PALLET_MAX_BOX_QTY = 50; // 1개의 팔레트에는 50박스 적재 가능

    private final WarehouseRepository warehouseRepository;
    private final SectionRepository sectionRepository;
    private final PalletRepository palletRepository;
    private final PalletCodeGenerator palletCodeGenerator;
    private final WarehouseCodeGenerator warehouseCodeGenerator;

    // 창고 등록
    @Transactional
    public Long createWarehouse(WarehouseCreateRequest req) {
        if (warehouseRepository.findByName(req.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 창고명입니다.");
        }
        if (warehouseRepository.findByAddress(req.getAddress()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 주소입니다.");
        }

        Warehouse warehouse = warehouseRepository.save(
                Warehouse.builder()
                        .code(warehouseCodeGenerator.nextCode())
                        .name(req.getName())
                        .address(req.getAddress())
                        .build()
        );

        Section cold = sectionRepository.save(
                Section.builder()
                        .code(warehouse.getCode() + "-COLD")
                        .type(SectionType.COLD)
                        .warehouse(warehouse)
                        .build()
        );

        Section normal = sectionRepository.save(
                Section.builder()
                        .code(warehouse.getCode() + "-NORMAL")
                        .type(SectionType.NORMAL)
                        .warehouse(warehouse)
                        .build()
        );

        createPallets(cold);
        createPallets(normal);

        return warehouse.getId();
    }

    private void createPallets(Section section) {
        for (int i = 0; i < PALLETS_PER_SECTION; i++) {
            palletRepository.save(
                    Pallet.builder()
                            .code(palletCodeGenerator.nextCode())
                            .maxBoxQty(PALLET_MAX_BOX_QTY)
                            .usedBoxQty(0)
                            .section(section)
                            .build()
            );
        }
    }

    // 창고 수정
    @Transactional
    public void updateWarehouse(Long id, WarehouseUpdateRequest req) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다."));
        warehouse.update(req.getName(), req.getAddress());
    }

    // 창고 폐쇄(논리적 삭제 -> 상태 = CLOSED 로 전환)
    @Transactional
    public void closeWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다."));
        warehouse.close();
    }
}