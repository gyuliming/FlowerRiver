package com.wms.flowerwms.outbound.service;

import com.wms.flowerwms.outbound.domain.Outbound;
import com.wms.flowerwms.outbound.domain.OutboundCodeGenerator;
import com.wms.flowerwms.outbound.dto.OutboundCreateRequest;
import com.wms.flowerwms.outbound.repository.OutboundRepository;
import com.wms.flowerwms.product.domain.Product;
import com.wms.flowerwms.product.repository.ProductRepository;
import com.wms.flowerwms.stock.domain.Stock;
import com.wms.flowerwms.stock.domain.StockHistory;
import com.wms.flowerwms.stock.domain.StockHistoryType;
import com.wms.flowerwms.stock.repository.StockHistoryRepository;
import com.wms.flowerwms.stock.repository.StockRepository;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import com.wms.flowerwms.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboundCommandService {

    private final OutboundRepository outboundRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final StockHistoryRepository stockHistoryRepository;
    private final OutboundCodeGenerator outboundCodeGenerator;

    @Transactional
    public Long createOutbound(OutboundCreateRequest req) {
        Warehouse warehouse = warehouseRepository.findById(req.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다."));

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // FIFO로 재고 있는 팔레트 목록 조회
        List<Stock> stocks = stockRepository
                .findByProductAndWarehouseOrderByInboundAtAsc(req.getProductId(), req.getWarehouseId());

        // 전체 재고 확인
        int totalStock = stocks.stream().mapToInt(Stock::getBoxQty).sum();
        if (req.getBoxQty() > totalStock) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + totalStock + " 박스");
        }

        // FIFO 출고 처리
        int remaining = req.getBoxQty();
        List<Outbound> outbounds = new ArrayList<>();

        for (Stock stock : stocks) {
            if (remaining <= 0) break;

            int deduct = Math.min(remaining, stock.getBoxQty());
            stock.subtract(deduct);
            stock.getPallet().subtractUsedBoxQty(deduct);
            remaining -= deduct;

            outbounds.add(Outbound.builder()
                    .code(outboundCodeGenerator.nextCode())
                    .warehouse(warehouse)
                    .section(stock.getSection())
                    .pallet(stock.getPallet())
                    .product(product)
                    .boxQty(deduct)
                    .build());
        }

        outboundRepository.saveAll(outbounds);

        // 재고 이력 저장
        stockHistoryRepository.save(StockHistory.builder()
                .warehouse(warehouse)
                .product(product)
                .type(StockHistoryType.OUTBOUND)
                .boxQty(req.getBoxQty())
                .build());

        return outbounds.get(0).getId();
    }
}