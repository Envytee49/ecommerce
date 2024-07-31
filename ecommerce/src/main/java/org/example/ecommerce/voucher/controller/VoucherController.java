package org.example.ecommerce.voucher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.voucher.dto.request.CreateProductVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateVoucherRequest;
import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.service.VoucherService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
public class VoucherController extends AbstractController {
    private final VoucherService voucherService;
    @GetMapping("/shop")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> getShopVouchers(@Valid @RequestBody FetchVoucherRequest request) {
        return respond(() -> voucherService.getShopVouchers(request));
    }
    @GetMapping("/redeem/{uuidVoucher}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> redeemVoucher(@PathVariable String uuidVoucher) {
        return respond(() -> voucherService.redeemVoucher(uuidVoucher), "Voucher redeemed successfully");
    }

    @PostMapping("/all-shop")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<?> createAllShopVoucher(@RequestBody @Valid CreateVoucherRequest request) {
        return respond(() -> voucherService.createAllShopVoucher(request), "Voucher created");
    }

    @PostMapping("/shop-products")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<?> createShopProductsVoucher(@RequestBody @Valid CreateProductVoucherRequest request) {
        return respond(() -> voucherService.createShopProductsVoucher(request), "Voucher created");
    }

    // how to make sure that the outsider
    // does not call api to this api having seller role
    // and delete system voucher
//    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/{uuidShop}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<?> deleteVoucher(@PathVariable String uuidShop) {
        return respond(() -> voucherService.delete(uuidShop), "Voucher deleted");
    }
}
