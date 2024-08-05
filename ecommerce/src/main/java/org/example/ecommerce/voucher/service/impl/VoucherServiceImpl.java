package org.example.ecommerce.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.constants.VoucherType;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.dto.response.ShopVoucherResponse;
import org.example.ecommerce.voucher.dto.response.VoucherResponse;
import org.example.ecommerce.voucher.model.*;
import org.example.ecommerce.voucher.projection.ProductVoucherProjection;
import org.example.ecommerce.voucher.repository.ProductVoucherRepository;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.repository.VoucherRepository;
import org.example.ecommerce.voucher.service.VoucherRedemptionService;
import org.example.ecommerce.voucher.service.VoucherService;
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
    private final CartRepository cartRepository;
    private final VoucherRedemptionService voucherRedemptionService;

    // check if voucher is claimed
    // check if current selected cart items is discount by voucher
    // if it is ALL_SHOP then is applicable
    @Override
    public ShopVoucherResponse getShopVouchers(FetchVoucherRequest request) {
        String uuidShop = request.getUuidShop();
        if (!shopRepository.existsById(uuidShop)) throw new AppException(ErrorCode.SHOP_NOT_FOUND);
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
        if (cartItems.size() != request.getUuidCartItems().size())
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);

        double subTotal = computeSubtotal(cartItems);
        // check constraint: subtotal > minspend, check if all_shop or for specific product, expired date
        for (VoucherResponse v : redeemedVouchers) {
            if (checkIsConstraintSatisfied(v, subTotal)) {
                // check if items in cart can be applied by this voucher
                if (v.getVoucherType() == VoucherType.PRODUCTS) {
                    // all the products of the shop that this voucher is applicable
                    List<ProductVoucherProjection> productVouchers =
                            productVoucherRepository
                                    .findByUuidVoucher(v.getUuidVoucher()); // how to reduce nums of db call
                    // all the selected products in the cart
                    List<String> uuidProducts =
                            cartItems.stream().map(CartItem::getUuidProduct).toList();
                    boolean isVoucherApplicable = checkIntersection(productVouchers, uuidProducts);
                    v.setApplicable(isVoucherApplicable);
                } else v.setApplicable(true);
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

    private boolean checkIntersection(List<ProductVoucherProjection> productVouchers, List<String> uuidProducts) {
        // Convert the list of product vouchers to a set of UUID strings
        List<String> productUuids = productVouchers.stream()
                .map(ProductVoucherProjection::getUuidProduct)
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
    // locking resource
    public void redeemVoucher(String uuidVoucher) {
        String uuidRedeemUser = SecurityUtils.getCurrentUserUuid();
        voucherRedemptionService.saveVoucherRedemption(uuidVoucher, uuidRedeemUser);
    }

    @Override
    public void updateVoucherUsage(List<String> uuidVouchers) {
        if (!uuidVouchers.isEmpty()) {
            List<VoucherRedemption> voucherRedemptions =
                    voucherRedemptionRepository.findByUuidVoucherIn(uuidVouchers);
            for (VoucherRedemption voucherRedemption : voucherRedemptions) {
                // if max usage is reached
                if (voucherRedemption.getUsage() < 1)
                    throw new AppException(ErrorCode.MAX_VOUCHER_USAGE_REACHED);
                voucherRedemption.setUsage(voucherRedemption.getUsage() - 1);
            }
            voucherRedemptionRepository.saveAll(voucherRedemptions);
        }
    }
}
