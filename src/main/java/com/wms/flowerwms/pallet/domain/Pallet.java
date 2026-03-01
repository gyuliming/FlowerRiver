package com.wms.flowerwms.pallet.domain;

import com.wms.flowerwms.section.domain.Section;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pallet",
        indexes = {
                @Index(name = "idx_pallet_section", columnList = "section_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Pallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private Integer maxBoxQty;

    @Column(nullable = false)
    private Integer usedBoxQty;

    // 동시성 제어
    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @PrePersist
    void onCreate() {
        if (usedBoxQty == null) usedBoxQty = 0;
        if (maxBoxQty == null) maxBoxQty = 0;
    }

    public int remainingBoxQty() {
        return Math.max(0, maxBoxQty - usedBoxQty);
    }

    public void increaseUsed(int addQty) {
        if (addQty <= 0) throw new IllegalArgumentException("추가 수량은 1 이상이어야 합니다.");
        if (usedBoxQty + addQty > maxBoxQty) throw new IllegalArgumentException("팔레트 최대 적재량을 초과했습니다.");
        usedBoxQty += addQty;
    }

    public void decreaseUsed(int minusQty) {
        if (minusQty <= 0) throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
        if (usedBoxQty - minusQty < 0) throw new IllegalArgumentException("사용 수량이 0 미만이 될 수 없습니다.");
        usedBoxQty -= minusQty;
    }

    // 팔레트 사용량 증가
    public void addUsedBoxQty(int qty) {
        this.usedBoxQty += qty;
    }
}