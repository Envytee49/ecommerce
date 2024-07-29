package org.example.ecommerce.voucher.service;

import org.example.ecommerce.voucher.dto.request.CreateProductVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateVoucherRequest;
import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.dto.response.ShopVoucherResponse;
import org.example.ecommerce.voucher.dto.response.SystemVoucherResponse;

import java.util.List;

public interface VoucherService {
    ShopVoucherResponse getShopVouchers(FetchVoucherRequest fetchVoucherRequest);
    void createAllShopVoucher(CreateVoucherRequest createVoucherRequest);
    void createShopProductsVoucher(CreateProductVoucherRequest createVoucherRequest);
    void delete(String uuidVoucher);
    void redeemVoucher(String uuidVoucher);
}
