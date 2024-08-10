package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.constants.PlatformVoucherType;
import org.example.ecommerce.common.constants.ShopVoucherType;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.service.DiscountService;
import org.example.ecommerce.voucher.projection.PlatformVoucherProjection;
import org.example.ecommerce.voucher.projection.RedeemedVoucherProjection;
import org.example.ecommerce.voucher.repository.PlatformVoucherRepository;
import org.example.ecommerce.voucher.repository.ProductVoucherRepository;
import org.example.ecommerce.voucher.repository.ShopVoucherRepository;
import org.example.ecommerce.voucher.service.VoucherService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final ShopVoucherRepository shopVoucherRepository;
    private final VoucherService voucherService;
    private final PlatformVoucherRepository platformVoucherRepository;
    private final CartRepository cartRepository;
    private final ProductVoucherRepository productVoucherRepository;
    private final ShippingServiceImpl shippingServiceImpl;

    @Override
    public double computeVoucherDiscount(double subTotal,
                                         Map<String, String> shopVouchers,
                                         String uuidShop,
                                         String discountCashbackVoucher,
                                         List<CartItem> cartItems) {
        log.info("Compute voucher discount");
        if (shopVouchers != null && shopVouchers.containsKey(uuidShop)) {
            String uuidShopVoucher = shopVouchers.get(uuidShop);
            if (!StringUtils.hasLength(uuidShopVoucher)) {
                throw new AppException(ErrorCode.BAD_REQUEST
                        .setMessage("shopVouchers is missing [key:uuidShop, value:uuidVoucher]"));
            }

            if (!shopVoucherRepository.existsById(uuidShopVoucher)) {
                throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
            }

            // check is user has redeemed this
            RedeemedVoucherProjection redeemedVoucher =
                    shopVoucherRepository
                            .findRedeemedVoucher(uuidShopVoucher, SecurityUtils.getCurrentUserUuid())
                            .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST));
            // check if voucher is qualified
            if (!voucherService.checkConstraintSatisfied(
                    redeemedVoucher.getMinSpend(),
                    redeemedVoucher.getValidUntil(),
                    redeemedVoucher.getValidFrom(),
                    redeemedVoucher.getUsage(),
                    subTotal))
                throw new AppException(ErrorCode.BAD_REQUEST);

            double voucherDiscount = computeShopVoucherDiscount(
                    redeemedVoucher,
                    cartItems
            );

            voucherDiscount = computePlatformVoucherDiscount(voucherDiscount, discountCashbackVoucher, cartItems);

            return voucherDiscount;
        }
        return computePlatformVoucherDiscount(0, discountCashbackVoucher, cartItems);
    }

    private double computeShopVoucherDiscount(RedeemedVoucherProjection shopVoucher,
                                              List<CartItem> cartItems) {
        log.info("Compute shop voucher discount");
        double voucherDiscount = 0;
        String uuidShopVoucher = shopVoucher.getUuidShopVoucher();
        DiscountType discountType = shopVoucher.getDiscountType();
        double discount = shopVoucher.getDiscountType() == DiscountType.FIXED ?
                shopVoucher.getDiscountValue() :
                shopVoucher.getDiscountPercentage();
        double discountCap = shopVoucher.getDiscountCap() == null ? 0 : shopVoucher.getDiscountCap();
        ShopVoucherType shopVoucherType = shopVoucher.getShopVoucherType();

        if (shopVoucherType == ShopVoucherType.ALL_SHOP)
            voucherDiscount = discountType.getDiscount(discount, Utils.computeCartItemSubTotal(cartItems), discountCap);
        else if (shopVoucherType == ShopVoucherType.PRODUCTS) {
            List<String> voucherApplicableCartItemProductUuids =
                    productVoucherRepository
                            .findVoucherProducts(uuidShopVoucher, cartItems
                                    .stream()
                                    .map(CartItem::getUuidCartItem)
                                    .toList());

            List<CartItem> voucherApplicableCartItems = cartItems.stream()
                    .filter(cartItem -> voucherApplicableCartItemProductUuids.contains(cartItem.getUuidProduct()))
                    .toList();
            if (voucherApplicableCartItemProductUuids.isEmpty())
                throw new AppException(ErrorCode.BAD_REQUEST);
            voucherDiscount = discountType.getDiscount(
                    discount,
                    Utils.computeCartItemSubTotal(voucherApplicableCartItems),
                    discountCap);
        }
        return voucherDiscount;
    }

    private double computePlatformVoucherDiscount(double voucherDiscount,
                                                  String discountCashbackVoucher,
                                                  List<CartItem> cartItems) {
        log.info("Compute platform voucher discount");
        // if client did not pass the platform voucher -> return the computed shop voucher discount
        if (!StringUtils.hasLength(discountCashbackVoucher)) return voucherDiscount;

        PlatformVoucherProjection platformVoucher = getPlatformVoucher(discountCashbackVoucher, PlatformVoucherType.DISCOUNT_CASHBACK);
        double discount = platformVoucher.getDiscountType() == DiscountType.FIXED ?
                platformVoucher.getDiscountValue() :
                platformVoucher.getDiscountPercentage();
        // if voucher is linked to a category
        String uuidCategory = platformVoucher.getUuidCategory();
        if (!StringUtils.hasLength(uuidCategory)) {
            double cartItemSubtotal = Utils.computeCartItemSubTotal(cartItems);
            double cartItemSubtotalWithShopDiscount = cartItemSubtotal - voucherDiscount;

            return platformVoucher
                    .getDiscountType()
                    .getDiscount(discount, cartItemSubtotalWithShopDiscount, platformVoucher.getDiscountCap());
        }
        List<String> uuidCartItem = cartItems.stream().map(CartItem::getUuidProduct).toList();
        List<CartItem> categoryMatchingItems = getCategoryMatchingItems(uuidCartItem, uuidCategory);
        // subtotal of all matched category items minus the voucherDiscount from shop voucher
        double categoryMatchingItemSubtotal = Utils.computeCartItemSubTotal(categoryMatchingItems);
        double categoryMatchingItemSubtotalWithShopDiscount = categoryMatchingItemSubtotal - voucherDiscount;

        return platformVoucher
                .getDiscountType()
                .getDiscount(discount, categoryMatchingItemSubtotalWithShopDiscount, platformVoucher.getDiscountCap());
    }

    @Override
    public double computeShippingDiscount(String freeShippingVoucher,
                                          String uuidUAddress,
                                          String uuidShop,
                                          double shippingSubtotal,
                                          List<CartItem> cartItems) {
        log.info("Compute shipping discount");
        if (!StringUtils.hasLength(freeShippingVoucher)) return 0;
        PlatformVoucherProjection platformVoucher = getPlatformVoucher(freeShippingVoucher, PlatformVoucherType.FREE_SHIPPING);
        double discount = platformVoucher.getDiscountType() == DiscountType.FIXED ?
                platformVoucher.getDiscountValue() :
                platformVoucher.getDiscountPercentage();
        // if voucher is not linked to a category
        String uuidCategory = platformVoucher.getUuidCategory();
        if (!StringUtils.hasLength(uuidCategory)) return platformVoucher
                .getDiscountType()
                .getDiscount(discount, shippingSubtotal, platformVoucher.getDiscountCap());

        // if voucher is linked to a category
        List<String> uuidCartItem = cartItems.stream().map(CartItem::getUuidProduct).toList();
        List<CartItem> categoryMatchingItems = getCategoryMatchingItems(uuidCartItem, uuidCategory);

        // subtotal of all matched category items minus the voucherDiscount from shop voucher
        double categoryMatchingItemShippingSubtotal = shippingServiceImpl.computeShipping(
                uuidUAddress,
                uuidShop,
                categoryMatchingItems);

        return platformVoucher
                .getDiscountType()
                .getDiscount(discount, categoryMatchingItemShippingSubtotal, platformVoucher.getDiscountCap());
    }

    private List<CartItem> getCategoryMatchingItems(List<String> uuidCartItem, String uuidCategory) {
        List<CartItem> categoryMatchingItems = cartRepository.findCategoryMatchingItems(uuidCartItem, uuidCategory);
        // if the client pass down a voucher that linked to a category but the query result is empty
        // mean that the voucher is incorrectly passed from the client
        if (categoryMatchingItems.isEmpty())
            throw new AppException(ErrorCode.BAD_REQUEST);
        return categoryMatchingItems;
    }

    private PlatformVoucherProjection getPlatformVoucher(String uuidVoucher, PlatformVoucherType expectedType) {
        PlatformVoucherProjection platformVoucher = platformVoucherRepository
                .findPlatformVoucher(uuidVoucher, SecurityUtils.getCurrentUserUuid())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (platformVoucher.getPlatformVoucherType() != expectedType) {
            throw new AppException(ErrorCode.BAD_REQUEST.setMessage("Incorrect platformVoucherType"));
        }
        return platformVoucher;
    }

}
