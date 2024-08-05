package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.dto.response.VoucherResponse;
import org.example.ecommerce.voucher.model.Voucher;
import org.example.ecommerce.voucher.projection.VoucherConstraintProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, String> {
    @Query(value = "SELECT new org.example.ecommerce.voucher.dto.response.VoucherResponse(" +
            "v.uuidVoucher, v.voucherName, v.discount, v.description, v.voucherType, vc.minSpend, vc.maxUsage, vc.validUntil, vc.validFrom)  " +
            "FROM Voucher v " +
            " INNER JOIN VoucherConstraint vc ON vc.uuidVoucher = v.uuidVoucher " +
            "WHERE v.uuidShop = :uuidShop AND v.isVisible = true")
    List<VoucherResponse> findVoucherByUuidShop(String uuidShop);
    @Query(value = "SELECT new org.example.ecommerce.voucher.dto.response.VoucherResponse(" +
            "v.uuidVoucher, v.voucherName, v.discount, v.description, v.voucherType, vc.minSpend, vr.usage, vc.maxUsage, vc.validUntil, vc.validFrom)  " +
            "FROM Voucher v " +
            "INNER JOIN VoucherConstraint vc ON vc.uuidVoucher = v.uuidVoucher " +
            "INNER JOIN VoucherRedemption vr ON vr.uuidVoucher = v.uuidVoucher AND vr.uuidUser = :uuidUser " +
            "WHERE v.uuidShop = :uuidShop")
    List<VoucherResponse> findRedeemedVoucher(String uuidShop, String uuidUser);

    @Query(value = "SELECT new org.example.ecommerce.voucher.dto.response.VoucherResponse(" +
            "v.uuidVoucher, v.voucherName, v.discount, v.description, v.voucherType, vc.minSpend, vc.maxUsage, vc.validUntil, vc.validFrom, false)  " +
            "FROM Voucher v " +
            "INNER JOIN VoucherConstraint vc ON vc.uuidVoucher = v.uuidVoucher " +
            "WHERE v.uuidShop = :uuidShop AND v.uuidVoucher NOT IN :uuidVouchers")
    List<VoucherResponse> findUnredeemedVoucher(List<String> uuidVouchers, String uuidShop);

    @Query(value = "SELECT v.uuidVoucher as uuidVoucher, vc.maxUsage as maxUsage " +
            "FROM Voucher v " +
            "INNER JOIN VoucherConstraint vc ON vc.uuidVoucher = v.uuidVoucher " +
            "WHERE v.uuidVoucher IN :uuidVouchers")
    List<VoucherConstraintProjection> findVoucherConstraint(List<String> uuidVouchers);

    Optional<Voucher> findByVoucherCode(String voucherCode);
}
