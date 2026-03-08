package com.wms.flowerwms.member.repository;

import com.wms.flowerwms.member.domain.Member;
import com.wms.flowerwms.member.domain.MemberRole;
import com.wms.flowerwms.member.domain.MemberStatus;
import com.wms.flowerwms.member.query.dto.MemberListRow;
import com.wms.flowerwms.warehouse.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByRole(MemberRole role);
    Optional<Member> findByLoginId(String loginId);
    boolean existsByWarehouseAndStatus(Warehouse warehouse, MemberStatus status);
    Optional<Member> findByWarehouseId(Long warehouseId);


    @Query("""
    select new com.wms.flowerwms.member.query.dto.MemberListRow(
        m.id, m.loginId, m.name, m.phone, m.email,
        m.role, m.status,
        case when m.warehouse is null then null else m.warehouse.name end,
        m.createdAt
    )
    from Member m
    left join m.warehouse
    where m.role != 'ADMIN'
    and m.status = :status
    order by m.createdAt desc
    """)
    Page<MemberListRow> findAllMembersByStatus(@Param("status") MemberStatus status, Pageable pageable);

    @Query("""
    select new com.wms.flowerwms.member.query.dto.MemberListRow(
        m.id, m.loginId, m.name, m.phone, m.email,
        m.role, m.status,
        case when m.warehouse is null then null else m.warehouse.name end,
        m.createdAt
    )
    from Member m
    left join m.warehouse
    where m.role != 'ADMIN'
    order by m.createdAt desc
    """)
    Page<MemberListRow> findAllMembers(Pageable pageable);
}