package org.example.ecommerce.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.constants.VoucherType;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.user.repository.ShopRepository;
import org.example.ecommerce.voucher.dto.request.CreateProductVoucherRequest;
import org.example.ecommerce.voucher.dto.request.CreateVoucherRequest;
import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.dto.response.ShopVoucherResponse;
import org.example.ecommerce.voucher.dto.response.VoucherResponse;
import org.example.ecommerce.voucher.model.*;
import org.example.ecommerce.voucher.projection.IProductVoucher;
import org.example.ecommerce.voucher.repository.ProductVoucherRepository;
import org.example.ecommerce.voucher.repository.VoucherConstraintRepository;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.repository.VoucherRepository;
import org.example.ecommerce.voucher.service.VoucherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    private final ProductVoucherRepository productVoucherRepository;
    private final ShopRepository shopRepository;
    private final VoucherConstraintRepository voucherConstraintRepository;
    private final CartRepository cartRepository;

    // check if voucher is claimed
    // check if current selected cart items is discount by voucher
    // if it is ALL_SHOP then is applicable
    @Override
    public ShopVoucherResponse getShopVouchers(FetchVoucherRequest request) {
        String uuidShop = request.getUuidShop();
        if(!shopRepository.existsById(uuidShop)) throw new AppException(ErrorCode.SHOP_NOT_FOUND);
        String uuidUser = SecurityUtils.getCurrentUserUuid();
        List<VoucherResponse> redeemedVouchers = voucherRepository.findRedeemedVoucher(uuidShop, uuidUser);

        // if the list is empty - did not redeem any - then all vouchers are not applicable
        if (redeemedVouchers.isEmpty()) {
            return ShopVoucherResponse
                    .builder()
                    .shopVouchers(
                            voucherRepository.findVoucherByUuidShop(uuidShop))
                    .build();
        }
        List<CartItem> cartItems = cartRepository.findAllById(request.getUuidCartItems());
        double subTotal = computeSubtotal(cartItems);
        // check constraint: subtotal > minspend, check if all_shop or for specific product, expired date
        for (VoucherResponse v : redeemedVouchers) {
            if (checkIsConstraintSatisfied(v, subTotal)) {
                // check if items in cart can be applied by this voucher
                if (v.getVoucherType() == VoucherType.PRODUCTS) {
                    // all the products of the shop that this voucher is applicable
                    List<IProductVoucher> productVouchers =
                            productVoucherRepository
                                    .findByUuidVoucher(v.getUuidVoucher()); // how to reduce nums of db call
                    // all the selected products in the cart
                    List<String> uuidProducts =
                            cartItems.stream().map(CartItem::getUuidProduct).toList();
                    boolean isVoucherApplicable = checkIntersection(productVouchers, uuidProducts);
                    v.setApplicable(isVoucherApplicable);
                }else v.setApplicable(true);
            }
        }

        List<String> uuidRedeemedVouchers =
                redeemedVouchers.stream().map(VoucherResponse::getUuidVoucher).toList();
        // get the unredeemed vouchers
        List<VoucherResponse> voucherResponses =
                voucherRepository.findUnredeemedVoucher(uuidRedeemedVouchers, uuidShop);
        // add the redeemed voucher to get the full vouchers of that shop
        voucherResponses.addAll(redeemedVouchers);
        return ShopVoucherResponse.builder()
                .shopVouchers(voucherResponses)
                .build();
    }

    private double computeSubtotal(List<CartItem> cartItems) {
        return cartItems.stream().mapToDouble(cartItem -> cartItem.getUnitPrice() * cartItem.getQuantity()).sum();
    }

    private boolean checkIntersection(List<IProductVoucher> productVouchers, List<String> uuidProducts) {
        // Convert the list of product vouchers to a set of UUID strings
        List<String> productUuids = productVouchers.stream()
                .map(IProductVoucher::getUuidProduct)
                .toList();

        // Check if any UUID in the uuidProducts list exists in the voucherUuids set
        return uuidProducts.stream()
                .anyMatch(productUuids::contains);
    }

    private boolean checkIsConstraintSatisfied(VoucherResponse v, double subTotal) {
        return v.getMinSpend() <= subTotal &&
                v.getValidUntil().isAfter(LocalDateTime.now()) &&
                v.getValidFrom().isBefore(LocalDateTime.now()) &&
                v.getUsage() < v.getMaxUsage();
    }

    @Override
    @Transactional
    public void createAllShopVoucher(CreateVoucherRequest request) {
        String uuidShop = shopRepository.findByUuidSeller(SecurityUtils.getCurrentUserUuid()).getUuidShop();
        if(voucherRepository.findByVoucherCode(request.getVoucherCode()).isPresent())
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
        if(voucherRepository.findByVoucherCode(request.getVoucherCode()).isPresent()) {
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTED);
        }        Voucher voucher = getVoucher(uuidShop, request);
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

    private Voucher getVoucher(String uuidShop, CreateVoucherRequest request) {
        return Voucher.builder()
                .voucherName(request.getVoucherName())
                .voucherCode(request.getVoucherCode())
                .quantity(request.getQuantity())
                .discountType(request.getDiscountType())
                .description(request.getDescription())
                .uuidShop(uuidShop)
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
        voucherRepository.deleteById(uuidVoucher);
    }

    @Override
    @Transactional
    // locking resource
    public void redeemVoucher(String uuidVoucher) {
        Voucher voucher = voucherRepository.findById(uuidVoucher)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if(voucherRedemptionRepository.findById(VoucherRedemptionId.builder()
                        .uuidVoucher(uuidVoucher)
                        .uuidUser(SecurityUtils.getCurrentUserUuid())
                .build()).isPresent())
            throw new AppException(ErrorCode.VOUCHER_ALREADY_REDEEMED);

        if (voucher.getQuantity() - 1 < 0)
            throw new AppException(ErrorCode.NOT_ENOUGH_VOUCHER);

        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRedemptionRepository.save(
                VoucherRedemption
                        .builder()
                        .uuidUser(SecurityUtils.getCurrentUserUuid())
                        .uuidVoucher(voucher.getUuidVoucher())
                        .usage(0)
                        .build());
        voucherRepository.save(voucher);
    }
}
