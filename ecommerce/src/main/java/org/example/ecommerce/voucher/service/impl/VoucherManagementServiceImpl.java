package org.example.ecommerce.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.constants.PlatformVoucherType;
import org.example.ecommerce.common.constants.ShopVoucherType;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.user.repository.UserRepository;
import org.example.ecommerce.voucher.aop.VoucherInfoValidation;
import org.example.ecommerce.voucher.dto.request.CreatePlatformVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateProductVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateVoucherRequest;
import org.example.ecommerce.voucher.dto.request.VoucherDistributionRequest;
import org.example.ecommerce.voucher.model.*;
import org.example.ecommerce.voucher.projection.VoucherConstraintProjection;
import org.example.ecommerce.voucher.repository.*;
import org.example.ecommerce.voucher.service.VoucherManagementService;
import org.example.ecommerce.voucher.service.VoucherRedemptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherManagementServiceImpl implements VoucherManagementService {
    private static final Logger log = LoggerFactory.getLogger(VoucherManagementServiceImpl.class);
    private final ShopVoucherRepository shopVoucherRepository;
    private final ProductVoucherRepository productVoucherRepository;
    private final ShopRepository shopRepository;
    private final VoucherConstraintRepository voucherConstraintRepository;
    private final ProductRepository productRepository;
    private final VoucherRedemptionService voucherRedemptionService;
    private final VoucherInfoRepository voucherInfoRepository;
    private final UserRepository userRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    private final PlatformVoucherRepository platformVoucherRepository;
    private final VoucherInfoValidation voucherInfoValidation;
    @Override
    @Transactional
    public void createAllShopVoucher(CreateVoucherRequest request) {
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        voucherInfoValidation.checkVoucherCodeExist(request.getVoucherCode());
        voucherInfoValidation.checkDiscountValueValid(request.getDiscountType(), request.getDiscountPercentage());
        VoucherInfo voucherInfo = createVoucherInfo(request);
        VoucherConstraint voucherConstraint = createVoucherConstraint(request);
        ShopVoucher shopVoucher = ShopVoucher.builder()
                .uuidShop(uuidShop)
                .uuidVoucherInfo(voucherInfo.getUuidVoucherInfo())
                .uuidVoucherConstraint(voucherConstraint.getUuidVoucherConstraint())
                .shopVoucherType(ShopVoucherType.ALL_SHOP)
                .build();
        voucherInfoRepository.save(voucherInfo);
        voucherConstraintRepository.save(voucherConstraint);
        shopVoucherRepository.save(shopVoucher);
    }

    private VoucherInfo createVoucherInfo(CreateVoucherRequest request) {
        return VoucherInfo.builder()
                .voucherCode(request.getVoucherCode())
                .discountCap(request.getDiscountCap())
                .description(request.getDescription())
                .discountPercentage(request.getDiscountPercentage() / 100.0)
                .discountValue(request.getDiscountValue())
                .isVisible(request.getIsVisible())
                .quantity(request.getQuantity())
                .build();
    }

    @Override
    @Transactional
    public void createShopProductsVoucher(CreateProductVoucherRequest request) {
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        voucherInfoValidation.checkVoucherCodeExist(request.getVoucherCode());
        voucherInfoValidation.checkDiscountValueValid(request.getDiscountType(), request.getDiscountPercentage());

        List<Product> products = productRepository.findAllById(request.getUuidProducts());
        products.forEach(product -> {
            if (!product.getUuidShop().equals(uuidShop)) {
                throw new AppException(ErrorCode.BAD_REQUEST);
            }
        });

        if (products.size() != request.getUuidProducts().size())
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);

        VoucherInfo voucherInfo = createVoucherInfo(request);
        VoucherConstraint voucherConstraint = createVoucherConstraint(request);
        ShopVoucher shopVoucher = ShopVoucher.builder()
                .uuidShop(uuidShop)
                .uuidVoucherInfo(voucherInfo.getUuidVoucherInfo())
                .uuidVoucherConstraint(voucherConstraint.getUuidVoucherConstraint())
                .shopVoucherType(ShopVoucherType.PRODUCTS)
                .build();
        List<ProductVoucher> productVouchers =
                request.getUuidProducts().stream().map(uuidProduct ->
                        ProductVoucher
                                .builder()
                                .uuidProduct(uuidProduct)
                                .uuidVoucher(shopVoucher.getUuidVoucher())
                                .build()
                ).toList();
        voucherInfoRepository.save(voucherInfo);
        voucherConstraintRepository.save(voucherConstraint);
        shopVoucherRepository.save(shopVoucher);
        productVoucherRepository.saveAll(productVouchers);
    }

    @Override
    public void createFreeShippingVoucher(CreatePlatformVoucherRequest request) {
        voucherInfoValidation.preCheck(request.getVoucherCode(),
                request.getUuidCategory(),
                request.getDiscountType(),
                request.getDiscountPercentage());

        VoucherInfo voucherInfo = createVoucherInfo(request);
        VoucherConstraint voucherConstraint = createVoucherConstraint(request);
        PlatformVoucher platformVoucher = PlatformVoucher.builder()
                .uuidVoucherInfo(voucherInfo.getUuidVoucherInfo())
                .uuidCategory(request.getUuidCategory())
                .uuidVoucherConstraint(voucherConstraint.getUuidVoucherConstraint())
                .platformVoucherType(PlatformVoucherType.FREE_SHIPPING)
                .build();

        voucherInfoRepository.save(voucherInfo);
        voucherConstraintRepository.save(voucherConstraint);
        platformVoucherRepository.save(platformVoucher);
    }
    private VoucherConstraint createVoucherConstraint(CreateVoucherRequest request) {
        return VoucherConstraint
                .builder()
                .maxUsage(request.getMaxUsage())
                .minSpend(request.getMinSpend())
                .validFrom(request.getValidFrom())
                .validUntil(request.getValidUntil())
                .build();
    }


    @Transactional
    @Override
    public void delete(String uuidVoucher) {
        ShopVoucher shopVoucher = shopVoucherRepository.findById(uuidVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        shopVoucherRepository.delete(shopVoucher);
    }

    @Override
    @Transactional
    public void giftVoucher(String uuidVoucher, String uuidUser) {
        voucherRedemptionService.saveShopVoucherRedemption(uuidVoucher, uuidUser);
    }

    @Override
    public void distributeVoucher(VoucherDistributionRequest request) {
        log.info("distributing voucher");
        List<VoucherConstraintProjection> vouchers = platformVoucherRepository.findVoucherConstraint(request.getUuidVouchers());
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
