package org.example.ecommerce.voucher.service;

public interface VoucherRedemptionService {
    void saveShopVoucherRedemption(String uuidVoucher, String uuidUser);
}
