package org.example.ecommerce.order.repository;

import org.example.ecommerce.order.model.CancelledOrderReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelledOrderReasonRepository extends JpaRepository<CancelledOrderReason, String> {
}
