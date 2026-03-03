package com.wms.flowerwms.stock.domain;

import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.product.domain.Product;
import com.wms.flowerwms.section.domain.Section;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock", indexes = {
        @Index(name = "idx_stock_product_id", columnList = "product_id"),
        @Index(name = "idx_stock_pallet_id", columnList = "pallet_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pallet_id")
    private Pallet pallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int boxQty;

    @Column(nullable = false)
    private LocalDateTime inboundAt;

    public void add(int qty) {
        this.boxQty += qty;
    }

    public void subtract(int qty) {
        if (qty > this.boxQty) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + this.boxQty + " 박스");
        }
        this.boxQty -= qty;
    }
}