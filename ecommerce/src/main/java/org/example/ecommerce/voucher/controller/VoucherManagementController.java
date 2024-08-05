package org.example.ecommerce.voucher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.voucher.dto.request.CreateProductVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateVoucherRequest;
import org.example.ecommerce.voucher.dto.request.VoucherDistributionRequest;
import org.example.ecommerce.voucher.service.VoucherManagementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers/management")
@RequiredArgsConstructor
public class VoucherManagementController extends AbstractController {
    private final VoucherManagementService voucherService;
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

    @PostMapping("/free-shipping")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> createFreeShippingVoucher(@RequestBody @Valid CreateVoucherRequest request) {
        return respond(() -> voucherService.createFreeShippingVoucher(request), "Voucher created");
    }

    @DeleteMapping("/{uuidShop}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<?> deleteVoucher(@PathVariable String uuidShop) {
        return respond(() -> voucherService.delete(uuidShop), "Voucher deleted");
    }

    @PostMapping("/gift/{uuidVoucher}/{uuidUser}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<?> giftVoucher(@PathVariable String uuidUser,
                                      @PathVariable String uuidVoucher) {
        return respond(() -> voucherService.giftVoucher(uuidVoucher, uuidUser), "Voucher gifted");
    }

    @PostMapping("/distribution")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> distributeVoucher(@RequestBody VoucherDistributionRequest request) {
        return respond(() -> voucherService.distributeVoucher(request), "Voucher distributed");
    }
}
