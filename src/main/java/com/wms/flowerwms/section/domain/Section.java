package com.wms.flowerwms.section.domain;

import com.wms.flowerwms.warehouse.domain.Warehouse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "section",
        indexes = {
                @Index(name = "idx_section_warehouse", columnList = "warehouse_id"),
                @Index(name = "idx_section_type", columnList = "type")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SectionType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
}
