package org.example.ecommerce.voucher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.service.VoucherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
public class VoucherController extends AbstractController {
    private final VoucherService voucherService;
    @GetMapping("/info/shop")
    public ApiResponse<?> getShopVouchers(@Valid @RequestBody FetchVoucherRequest request) {
        return respond(() -> voucherService.getShopVouchers(request));
    }

    @GetMapping("/info/free-shipping")
    public ApiResponse<?> getFreeShippingVouchers() {
        return respond(() -> voucherService.getFreeShippingVouchers());
    }

    @GetMapping("/info/discount-cashback")
    public ApiResponse<?> getDiscountCashbackVouchers() {
        return respond(() -> voucherService.getDiscountCashbackVouchers());
    }
    @PostMapping("/redeem/{uuidVoucher}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> redeemVoucher(@PathVariable String uuidVoucher) {
        return respond(() -> voucherService.redeemVoucher(uuidVoucher), "Voucher redeemed successfully");
    }
}
