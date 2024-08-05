package org.example.ecommerce.voucher.service;

import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.dto.response.ShopVoucherResponse;

import java.util.List;

public interface VoucherService {
    ShopVoucherResponse getShopVouchers(FetchVoucherRequest fetchVoucherRequest);
    void redeemVoucher(String uuidVoucher);
    void updateVoucherUsage(List<String> uuidVouchers);
}
