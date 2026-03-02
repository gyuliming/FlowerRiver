package com.wms.flowerwms.stock.repository;

import com.wms.flowerwms.stock.domain.Stock;
import com.wms.flowerwms.pallet.domain.Pallet;
import com.wms.flowerwms.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductAndPallet(Product product, Pallet pallet);
}