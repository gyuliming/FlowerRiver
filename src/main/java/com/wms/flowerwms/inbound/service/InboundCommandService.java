package com.wms.flowerwms.inbound.service;

import com.wms.flowerwms.inbound.domain.Inbound;
import com.wms.flowerwms.inbound.domain.InboundCodeGenerator;
import com.wms.flowerwms.inbound.dto.InboundCreateRequest;
import com.wms.flowerwms.inbound.repository.InboundRepository;
import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.pallet.repository.PalletRepository;
import com.wms.flowerwms.product.domain.Product;
import com.wms.flowerwms.product.repository.ProductRepository;
import com.wms.flowerwms.section.domain.Section;
import com.wms.flowerwms.section.repository.SectionRepository;
import com.wms.flowerwms.stock.domain.Stock;
import com.wms.flowerwms.stock.repository.StockRepository;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InboundCommandService {

    private final InboundRepository inboundRepository;
    private final WarehouseRepository warehouseRepository;
    private final PalletRepository palletRepository;
    private final InboundCodeGenerator inboundCodeGenerator;
    private final SectionRepository sectionRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Long createInbound(InboundCreateRequest req) {
        Warehouse warehouse = warehouseRepository.findById(req.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다."));

        Section section = sectionRepository.findById(req.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구역입니다."));

        Pallet pallet = palletRepository.findById(req.getPalletId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔레트입니다."));

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // storageType 검증 (상품 보관타입과 섹션 타입이 맞는지)
        if (product.getStorageType() != section.getType()) {
            throw new IllegalArgumentException("상품 보관 타입과 구역 타입이 맞지 않습니다.");
        }

        // 여유 공간 확인
        int available = pallet.getMaxBoxQty() - pallet.getUsedBoxQty();
        if (req.getBoxQty() > available) {
            throw new IllegalArgumentException("팔레트 여유 공간이 부족합니다.");
        }

        // 팔레트 사용량 증가
        pallet.addUsedBoxQty(req.getBoxQty());

        // 재고 반영 (없으면 새로 생성, 있으면 증가)
        Stock stock = stockRepository.findByProductAndPallet(product, pallet)
                .orElseGet(() -> Stock.builder()
                        .warehouse(warehouse)
                        .section(section)
                        .pallet(pallet)
                        .product(product)
                        .boxQty(0)
                        .build());

        stock.add(req.getBoxQty());
        stockRepository.save(stock);

        // 입고 이력 저장
        Inbound inbound = Inbound.builder()
                .code(inboundCodeGenerator.nextCode())
                .warehouse(warehouse)
                .section(section)
                .pallet(pallet)
                .product(product)
                .boxQty(req.getBoxQty())
                .build();

        return inboundRepository.save(inbound).getId();
    }
}