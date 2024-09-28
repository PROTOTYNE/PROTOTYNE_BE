package com.prototyne.repository;

import com.prototyne.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 기업 ID로 모든 시제품 조회
    List<Product> findByEnterpriseId(Long enterpriseId);
    Product findByEnterpriseIdAndId(Long enterpriseId, Long productId);
}
