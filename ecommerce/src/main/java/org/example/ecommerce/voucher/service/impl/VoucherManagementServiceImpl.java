package org.example.ecommerce.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.constants.VoucherType;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.user.repository.UserRepository;
import org.example.ecommerce.voucher.dto.request.CreateProductVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateVoucherRequest;
import org.example.ecommerce.voucher.dto.request.VoucherDistributionRequest;
import org.example.ecommerce.voucher.model.*;
import org.example.ecommerce.voucher.projection.VoucherConstraintProjection;
import org.example.ecommerce.voucher.repository.ProductVoucherRepository;
import org.example.ecommerce.voucher.repository.VoucherConstraintRepository;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.repository.VoucherRepository;
import org.example.ecommerce.voucher.service.VoucherManagementService;
import org.example.ecommerce.voucher.service.VoucherRedemptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherManagementServiceImpl implements VoucherManagementService {
    private final VoucherRepository voucherRepository;
    private final ProductVoucherRepository productVoucherRepository;
    private final ShopRepository shopRepository;
    private final VoucherConstraintRepository voucherConstraintRepository;
    private final ProductRepository productRepository;
    private final VoucherRedemptionService voucherRedemptionService;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createAllShopVoucher(CreateVoucherRequest request) {
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        if (voucherRepository.findByVoucherCode(request.getVoucherCode()).isPresent())
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        Voucher voucher = getVoucher(uuidShop, request);
        voucher.setVoucherType(VoucherType.ALL_SHOP);
        VoucherConstraint voucherConstraint = getVoucherConstraint(voucher.getUuidVoucher(), request);
        voucherRepository.save(voucher);
        voucherConstraintRepository.save(voucherConstraint);
    }

    @Override
    @Transactional
    public void createShopProductsVoucher(CreateProductVoucherRequest request) {
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        if (voucherRepository.findByVoucherCode(request.getVoucherCode()).isPresent()) {
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        }
        List<Product> products = productRepository.findAllById(request.getUuidProducts());
        products.forEach(product -> {
            if (!product.getUuidShop().equals(uuidShop)) {
                throw new AppException(ErrorCode.BAD_REQUEST);
            }
        });
        if (products.size() != request.getUuidProducts().size())
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

        Voucher voucher = getVoucher(uuidShop, request);
        voucher.setVoucherType(VoucherType.PRODUCTS);
        VoucherConstraint voucherConstraint = getVoucherConstraint(voucher.getUuidVoucher(), request);
        List<ProductVoucher> productVouchers =
                request.getUuidProducts().stream().map(uuidProduct ->
                        ProductVoucher
                                .builder()
                                .uuidProduct(uuidProduct)
                                .uuidVoucher(voucher.getUuidVoucher())
                                .build()
                ).toList();
        voucherRepository.save(voucher);
        productVoucherRepository.saveAll(productVouchers);
        voucherConstraintRepository.save(voucherConstraint);
    }

    @Override
    public void createFreeShippingVoucher(CreateVoucherRequest request) {
        if (voucherRepository.findByVoucherCode(request.getVoucherCode()).isPresent())
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        Voucher voucher = getVoucher(null, request);
        voucher.setVoucherType(VoucherType.FREE_SHIPPING);
        VoucherConstraint voucherConstraint = getVoucherConstraint(voucher.getUuidVoucher(), request);
        voucherRepository.save(voucher);
        voucherConstraintRepository.save(voucherConstraint);
    }

    private Voucher getVoucher(String uuidShop, CreateVoucherRequest request) {
        return Voucher.builder()
                .voucherName(request.getVoucherName())
                .voucherCode(request.getVoucherCode())
                .quantity(request.getQuantity())
                .discountType(request.getDiscountType())
                .description(request.getDescription())
                .uuidShop(uuidShop)
                .isVisible(request.getIsVisible())
                .build();
    }

    private VoucherConstraint getVoucherConstraint(String uuidVoucher, CreateVoucherRequest request) {
        return VoucherConstraint
                .builder()
                .uuidVoucher(uuidVoucher)
                .maxUsage(request.getMaxUsage())
                .minSpend(request.getMinSpend())
                .validFrom(request.getValidFrom())
                .validUntil(request.getValidUntil())
                .build();
    }

    @Transactional
    @Override
    public void delete(String uuidVoucher) {
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        Voucher voucher = voucherRepository.findById(uuidVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        if (!voucher.getUuidShop().equals(uuidShop)) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }
        voucherRepository.deleteById(uuidVoucher);
    }

    @Override
    @Transactional
    public void giftVoucher(String uuidVoucher, String uuidUser) {
        voucherRedemptionService.saveVoucherRedemption(uuidVoucher, uuidUser);
    }

    @Override
    public void distributeVoucher(VoucherDistributionRequest request) {
        List<VoucherConstraintProjection> vouchers = voucherRepository.findVoucherConstraint(request.getUuidVouchers());
        if (vouchers.size() != request.getUuidVouchers().size())
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        List<String> uuidUsers = userRepository.getAllUuids();
        List<VoucherRedemption> voucherRedemptions = new ArrayList<>();
        for (VoucherConstraintProjection voucher : vouchers) {
            for (String uuidUser : uuidUsers) {
                voucherRedemptions.add(
                        VoucherRedemption
                                .builder()
                                .uuidUser(uuidUser)
                                .uuidVoucher(voucher.getUuidVoucher())
                                .usage(voucher.getMaxUsage())
                                .build());
            }
        }
        voucherRedemptionRepository.saveAll(voucherRedemptions);
    }
}
