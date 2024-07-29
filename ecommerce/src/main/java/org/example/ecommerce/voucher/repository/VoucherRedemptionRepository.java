package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.model.VoucherRedemption;
import org.example.ecommerce.voucher.model.VoucherRedemptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRedemptionRepository extends JpaRepository<VoucherRedemption, VoucherRedemptionId> {

    List<VoucherRedemption> findByUuidVoucherIn(List<String> uuidVouchers);
}
