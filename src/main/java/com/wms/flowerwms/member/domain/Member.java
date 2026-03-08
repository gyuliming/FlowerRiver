package com.wms.flowerwms.member.domain;

import com.wms.flowerwms.warehouse.domain.Warehouse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member", indexes = {
        @Index(name = "idx_member_login_id", columnList = "loginId"),
        @Index(name = "idx_member_status", columnList = "status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        if (this.status == null) this.status = MemberStatus.PENDING;
        if (this.role == null) this.role = MemberRole.MANAGER;
    }

    public void approve(Warehouse warehouse) {
        this.status = MemberStatus.ACTIVE;
        this.warehouse = warehouse;
    }

    public void reject() {
        this.status = MemberStatus.REJECTED;
    }

    public void updateStatus(MemberStatus status) {
        this.status = status;
    }
}