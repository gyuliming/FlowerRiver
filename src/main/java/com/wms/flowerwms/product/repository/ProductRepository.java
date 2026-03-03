package com.wms.flowerwms.product.repository;

import com.wms.flowerwms.product.domain.FlowerType;
import com.wms.flowerwms.product.domain.Product;
import com.wms.flowerwms.product.query.dto.ProductListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);

    boolean existsByName(String name);

    @Query("""
    select new com.wms.flowerwms.product.query.dto.ProductListRow(
        p.id, p.code, p.name, p.type, p.storageType,
        p.pricePerBox, p.qtyPerBox, p.unit, p.imageUrl
    )
    from Product p
    where (:keyword is null or :keyword = ''
           or p.code like concat('%', :keyword, '%')
           or p.name like concat('%', :keyword, '%'))
    and (:type is null or p.type = :type)
    order by p.id desc
    """)
    Page<ProductListRow> searchProducts(
            @Param("keyword") String keyword,
            @Param("type") FlowerType type,
            Pageable pageable
    );
}