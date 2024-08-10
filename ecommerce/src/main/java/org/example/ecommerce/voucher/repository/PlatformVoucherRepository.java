package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.model.PlatformVoucher;
import org.example.ecommerce.voucher.projection.PlatformVoucherProjection;
import org.example.ecommerce.voucher.projection.VoucherConstraintProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlatformVoucherRepository extends JpaRepository<PlatformVoucher, String> {
    @Query(value = "SELECT " +
            "vf.discountValue as discountValue, " +
            "vf.discountPercentage as discountPercentage, " +
            "vf.discountCap as discountCap, " +
            "vf.discountType as discountType, " +
            "v.platformVoucherType as platformVoucherType, " +
            "v.uuidCategory as uuidCategory, " +
            "vc.minSpend as minSpend, " +
            "vr.usage, vc.maxUsage as maxUsage, " +
            "vc.validUntil as validUntil, " +
            "vc.validFrom as validFrom " +
            "FROM PlatformVoucher v " +
            "INNER JOIN VoucherConstraint vc ON vc.uuidVoucherConstraint = v.uuidVoucherConstraint " +
            "INNER JOIN VoucherInfo vf ON vf.uuidVoucherInfo = v.uuidVoucherInfo " +
            "INNER JOIN VoucherRedemption vr ON vr.uuidVoucher = v.uuidVoucher " +
            "WHERE v.uuidVoucher = :uuidVoucher AND vr.uuidUser = :uuidUser")
    Optional<PlatformVoucherProjection> findPlatformVoucher(String uuidVoucher,
                                                            String uuidUser);

    @Query(value = "SELECT v.uuidVoucher as uuidVoucher, vc.maxUsage as maxUsage " +
            "FROM PlatformVoucher v " +
            "INNER JOIN VoucherConstraint vc ON vc.uuidVoucherConstraint = v.uuidVoucherConstraint " +
            "WHERE v.uuidVoucher IN :uuidVouchers")
    List<VoucherConstraintProjection> findVoucherConstraint(List<String> uuidVouchers);
}
