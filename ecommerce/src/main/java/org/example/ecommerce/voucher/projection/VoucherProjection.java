package org.example.ecommerce.voucher.projection;

import org.example.ecommerce.common.constants.DiscountType;

import java.time.LocalDateTime;

public interface VoucherProjection {
    String getUuidShopVoucher();

    Double getDiscountValue();
    Double getDiscountPercentage();

    double getMinSpend();

    DiscountType getDiscountType();

    int getUsage();

    Double getDiscountCap();

    LocalDateTime getValidUntil();

    LocalDateTime getValidFrom();
}
