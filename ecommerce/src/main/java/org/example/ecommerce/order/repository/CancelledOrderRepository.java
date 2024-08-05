package org.example.ecommerce.order.repository;

import org.example.ecommerce.order.model.CancelledOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CancelledOrderRepository extends JpaRepository<CancelledOrder, String> {
    // order -> order item -> product -> shop -> seller

//    @Query(value = "SELECT oi.uuidOrderItem, co.requestedAt " +
//            "FROM OrderItem oi " +
//            "INNER JOIN CancelledOrder co ON co.uuidOrder = oi.uuidOrder " +
//            "WHERE oi.uuidShop = :uuidShop AND co.status = :status")
}
