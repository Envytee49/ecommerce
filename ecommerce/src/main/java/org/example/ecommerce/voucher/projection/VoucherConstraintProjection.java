package org.example.ecommerce.voucher.projection;

import org.example.ecommerce.common.constants.VoucherType;

public interface VoucherConstraintProjection {
    String getUuidVoucher();

    int getMaxUsage();

}