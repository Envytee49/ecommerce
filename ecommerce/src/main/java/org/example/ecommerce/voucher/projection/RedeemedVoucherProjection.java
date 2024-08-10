package org.example.ecommerce.voucher.projection;

import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.constants.ShopVoucherType;

public interface RedeemedVoucherProjection extends VoucherProjection {
    ShopVoucherType getShopVoucherType();

}
