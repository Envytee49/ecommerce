package org.example.ecommerce.voucher.service;

import org.example.ecommerce.voucher.dto.request.CreatePlatformVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateProductVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateVoucherRequest;
import org.example.ecommerce.voucher.dto.request.VoucherDistributionRequest;

public interface VoucherManagementService {
    void createAllShopVoucher(CreateVoucherRequest createVoucherRequest);
    void createShopProductsVoucher(CreateProductVoucherRequest createVoucherRequest);
    void delete(String uuidVoucher);
    void giftVoucher(String uuidVoucher, String uuidUser);
    void distributeVoucher(VoucherDistributionRequest request);
    void createFreeShippingVoucher(CreatePlatformVoucherRequest request);
}
