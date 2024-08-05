package org.example.ecommerce.voucher.service;

public interface VoucherRedemptionService {
    void saveVoucherRedemption(String uuidVoucher, String uuidUser);
}
