package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.model.ShopVoucher;
import org.example.ecommerce.voucher.projection.RedeemedVoucherProjection;
import org.example.ecommerce.voucher.projection.VoucherResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShopVoucherRepository extends JpaRepository<ShopVoucher, String> {
    @Query(value = "SELECT " +
            "v.uuidVoucher as uuidVoucher, " +
            "vf.discountValue as discountValue, " +
            "vf.discountPercentage as discountPercentage, " +
            "vf.description as description, " +
            "vf.discountCap as discountCap, " +
            "vf.discountType as discountType, " +
            "v.shopVoucherType as shopVoucherType, " +
            "vc.minSpend as minSpend, " +
            "vc.maxUsage as maxUsage, " +
            "vc.validUntil as validUntil, " +
            "vc.validFrom as validFrom, " +
            "vr.usage as remainingUsage, " +
            "CASE WHEN vr.uuidUser IS NULL THEN false ELSE true END AS isRedeemed " +
            "FROM ShopVoucher v " +
            "INNER JOIN VoucherConstraint vc ON vc.uuidVoucherConstraint = v.uuidVoucherConstraint AND vc.validUntil > LOCAL DATETIME " +
            "INNER JOIN VoucherInfo vf ON vf.uuidVoucherInfo = v.uuidVoucherInfo AND vf.isVisible = true " +
            "LEFT JOIN VoucherRedemption vr ON vr.uuidVoucher = v.uuidVoucher AND vr.uuidUser = :uuidUser AND vr.usage > 0 " +
            "WHERE v.uuidShop = :uuidShop ")
    List<VoucherResponseProjection> findVoucherByUuidShop(String uuidShop, String uuidUser);

    @Query(value = "SELECT " +
            "vf.discountValue as discountValue, " +
            "vf.discountPercentage as discountPercentage, " +
            "vf.discountType as discountType, " +
            "vf.discountCap as discountCap, " +
            "v.shopVoucherType as shopVoucherType, " +
            "v.uuidVoucher as uuidShopVoucher, " +
            "vc.minSpend as minSpend, " +
            "vr.usage as usage, vc.maxUsage as maxUsage, " +
            "vc.validUntil as validUntil, " +
            "vc.validFrom as validFrom " +
            "FROM ShopVoucher v " +
            "INNER JOIN VoucherConstraint vc ON vc.uuidVoucherConstraint = v.uuidVoucherConstraint " +
            "INNER JOIN VoucherInfo vf ON vf.uuidVoucherInfo = v.uuidVoucherInfo " +
            "INNER JOIN VoucherRedemption vr ON vr.uuidVoucher = v.uuidVoucher " +
            "WHERE v.uuidVoucher = :uuidVoucher AND vr.uuidUser = :uuidUser")
    Optional<RedeemedVoucherProjection> findRedeemedVoucher(String uuidVoucher, String uuidUser);


}
