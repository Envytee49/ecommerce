package org.example.ecommerce.voucher.projection;

import org.example.ecommerce.common.constants.PlatformVoucherType;

public interface PlatformVoucherProjection extends VoucherProjection {
    PlatformVoucherType getPlatformVoucherType();

    String getUuidCategory();
}
