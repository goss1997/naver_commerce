package com.naver.commerce.repository;

import com.naver.commerce.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, String> {

    boolean existsByProductOrderId(String productOrderId);
}
