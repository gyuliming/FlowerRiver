package com.wms.flowerwms.product.domain;

import com.wms.flowerwms.section.domain.SectionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product",
        indexes = {@Index(name = "idx_product_code", columnList = "code")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FlowerType type; // 절화, 화분, 가공화

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SectionType storageType;  // COLD or NORMAL

    @Column(nullable = false)
    private int pricePerBox; // 박스 당 가격

    @Column(nullable = false)
    private int qtyPerBox; // 박스 당 수량

    @Column(nullable = false, length = 20)
    private String unit; // 단위(단)

    private String imageUrl; // 이미지

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // type 에 따라 storageType 자동 결정
    public static SectionType resolveStorageType(FlowerType type) {
        return type == FlowerType.CUT ? SectionType.COLD : SectionType.NORMAL;
    }
}