package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.model.VoucherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherInfoRepository extends JpaRepository<VoucherInfo, String> {
    Optional<VoucherInfo> findByVoucherCode(String voucherCode);
}
