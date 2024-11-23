package com.example.order_management_system.repository;

import com.example.order_management_system.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM OrderProduct op WHERE op.product.id = :productId")
    void removeProductFromOrders(Long productId);

    @Query("SELECT op.order FROM OrderProduct op WHERE op.product.id = :productId")
    List<Order> findOrdersByProductId(@Param("productId") Long productId);
}
