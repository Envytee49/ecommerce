package org.example.ecommerce.voucher.projection;

import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.constants.ShopVoucherType;

import java.time.LocalDateTime;

public interface VoucherResponseProjection {
    String getUuidVoucher();
    Double getDiscountValue();
    Double getDiscountPercentage();
    Double getDiscountCap();
    DiscountType getDiscountType();
    String getDescription();
    ShopVoucherType getShopVoucherType();
    Double getMinSpend();
    Integer getMaxUsage();
    Integer getRemainingUsage();
    LocalDateTime getValidUntil();
    LocalDateTime getValidFrom();
    boolean getIsRedeemed();
}
