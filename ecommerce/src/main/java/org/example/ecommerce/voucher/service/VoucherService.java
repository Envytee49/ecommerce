package org.example.ecommerce.voucher.service;

import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.dto.response.DCVoucherResponse;
import org.example.ecommerce.voucher.dto.response.FSVoucherResponse;
import org.example.ecommerce.voucher.dto.response.ShopVoucherResponse;

import java.time.LocalDateTime;
import java.util.Map;

public interface VoucherService {
    ShopVoucherResponse getShopVouchers(FetchVoucherRequest fetchVoucherRequest);
    void redeemVoucher(String uuidVoucher);
    void updateVoucherUsage(Map<String, String> shopVouchers);
    boolean checkConstraintSatisfied(double minSpend,
                                     LocalDateTime validUntil,
                                     LocalDateTime validFrom,
                                     int usage,
                                     double subTotal);

    FSVoucherResponse getFreeShippingVouchers();

    DCVoucherResponse getDiscountCashbackVouchers();
}
