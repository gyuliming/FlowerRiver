package com.wms.flowerwms.outbound.service;

import com.wms.flowerwms.outbound.domain.Outbound;
import com.wms.flowerwms.outbound.domain.OutboundCodeGenerator;
import com.wms.flowerwms.outbound.dto.OutboundCreateRequest;
import com.wms.flowerwms.outbound.repository.OutboundRepository;
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
public class OutboundCommandService {

    private final OutboundRepository outboundRepository;
    private final WarehouseRepository warehouseRepository;
    private final SectionRepository sectionRepository;
    private final PalletRepository palletRepository;
    private final ProductRepository productRepository;
    private final OutboundCodeGenerator outboundCodeGenerator;
    private final StockRepository stockRepository;

    @Transactional
    public Long createOutbound(OutboundCreateRequest req) {

        Warehouse warehouse = warehouseRepository.findById(req.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다."));

        Section section = sectionRepository.findById(req.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구역입니다."));

        Pallet pallet = palletRepository.findById(req.getPalletId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔레트입니다."));

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 재고 확인
        Stock stock = stockRepository.findByProductAndPallet(product, pallet)
                .orElseThrow(() -> new IllegalArgumentException("해당 팔레트에 재고가 없습니다."));

        // 재고 감소 (stock.subtract 내부에서 부족하면 예외 발생)
        stock.subtract(req.getBoxQty());

        // 팔레트 사용량 감소
        pallet.subtractUsedBoxQty(req.getBoxQty());

        // 출고 이력 저장
        Outbound outbound = Outbound.builder()
                .code(outboundCodeGenerator.nextCode())
                .warehouse(warehouse)
                .section(section)
                .pallet(pallet)
                .product(product)
                .boxQty(req.getBoxQty())
                .build();

        return outboundRepository.save(outbound).getId();
    }
}