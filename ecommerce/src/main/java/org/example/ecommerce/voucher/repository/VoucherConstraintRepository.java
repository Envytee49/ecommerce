package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.model.VoucherConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherConstraintRepository extends JpaRepository<VoucherConstraint, String> {
}
