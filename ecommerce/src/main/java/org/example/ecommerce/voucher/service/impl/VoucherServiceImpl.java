package org.example.ecommerce.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.constants.ShopVoucherType;
import org.example.ecommerce.common.constants.VoucherMessage;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.voucher.dto.request.FetchVoucherRequest;
import org.example.ecommerce.voucher.dto.response.DCVoucherResponse;
import org.example.ecommerce.voucher.dto.response.FSVoucherResponse;
import org.example.ecommerce.voucher.dto.response.ShopVoucherResponse;
import org.example.ecommerce.voucher.dto.response.VoucherResponse;
import org.example.ecommerce.voucher.model.VoucherRedemption;
import org.example.ecommerce.voucher.projection.VoucherResponseProjection;
import org.example.ecommerce.voucher.repository.ProductVoucherRepository;
import org.example.ecommerce.voucher.repository.ShopVoucherRepository;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.service.VoucherRedemptionService;
import org.example.ecommerce.voucher.service.VoucherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class VoucherServiceImpl implements VoucherService {
    private final ShopVoucherRepository shopVoucherRepository;
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
        log.info("getShopVouchers");
        String uuidShop = request.getUuidShop();
        if (!shopRepository.existsById(uuidShop)) throw new AppException(ErrorCode.SHOP_NOT_FOUND);
        // user did not select any products
        if (request.getUuidCartItems() == null) {
            List<VoucherResponseProjection> voucherResponseProjections = shopVoucherRepository
                    .findVoucherByUuidShop(uuidShop, SecurityUtils.getCurrentUserUuid());
            log.info(String.valueOf(voucherResponseProjections.size()));
            List<VoucherResponse> voucherResponses = voucherResponseProjections
                    .stream()
                    .map(v -> {
                        VoucherResponse vp = VoucherResponse.from(v);
                        vp.setMessage(VoucherMessage.NO_PRODUCTS_SELECTED_MESSAGE);
                        return vp;
                    })
                    .toList();
            return ShopVoucherResponse
                    .builder()
                    .shopVouchers(voucherResponses)
                    .build();
        }
        // if the client pass down but the list is empty
        if (request.getUuidCartItems().isEmpty())
            throw new AppException(ErrorCode.BAD_REQUEST.setMessage("uuidCartItems must not be empty"));
        // if client pass down but the uuidCartItem is incorrect
        List<CartItem> cartItems = cartRepository.findShopCartItem(request.getUuidCartItems(), uuidShop);
        log.info("cartItems.size() {}", cartItems.size());
        if (cartItems.size() != request.getUuidCartItems().size())
            throw new AppException(ErrorCode.BAD_REQUEST.setMessage("Cart Item Not Found Or Not Belong To Shop"));
        // get all voucher of the current shop and current state of redemption
        List<VoucherResponseProjection> voucherResponseProjections = shopVoucherRepository
                .findVoucherByUuidShop(uuidShop, SecurityUtils.getCurrentUserUuid());

        List<VoucherResponse> voucherResponses = new ArrayList<>();
        // for each voucher of the shop check if its applicable
        for (VoucherResponseProjection v : voucherResponseProjections) {
            VoucherResponse voucherResponse = VoucherResponse.from(v);

            List<CartItem> applicableVoucherProduct = getApplicableVoucherProduct(
                    v.getShopVoucherType(),
                    cartItems,
                    v.getUuidVoucher());

            boolean isProductVoucherApplicable = applicableVoucherProduct != null;

            boolean isMinSpendReached = checkMinSpendReached(v.getMinSpend(), Utils.computeCartItemSubTotal(applicableVoucherProduct));
            String isValidDateFromTodayMessage = getValidDateFromTodayMessage(v.getValidFrom());

            boolean isApplicable = isProductVoucherApplicable &&
                    isMinSpendReached &&
                    isValidDateFromTodayMessage == null &&
                    v.getIsRedeemed();
            log.info(v.getUuidVoucher());
            String message = getMessage(isProductVoucherApplicable,
                    isValidDateFromTodayMessage,
                    isMinSpendReached,
                    v.getIsRedeemed());

            voucherResponse.setMessage(message);
            voucherResponse.setApplicable(isApplicable);

            voucherResponses.add(voucherResponse);
        }
        return ShopVoucherResponse
                .builder()
                .shopVouchers(voucherResponses)
                .build();
    }

    private List<CartItem> getApplicableVoucherProduct(ShopVoucherType shopVoucherType, List<CartItem> cartItems, String uuidVoucher) {
        if (shopVoucherType.equals(ShopVoucherType.PRODUCTS)) {
            // all the products of the shop that this voucher is applicable
            // how to reduce nums of db call
            List<String> uuidCartItems = cartItems.stream().map(CartItem::getUuidCartItem).toList();
            List<String> productVouchers = productVoucherRepository.findVoucherProducts(uuidVoucher, uuidCartItems);
            // all the selected products of the shop in the cart
            return productVouchers.isEmpty() ? null : cartItems
                    .stream()
                    .filter(cartItem -> productVouchers.contains(cartItem.getUuidProduct()))
                    .toList();
        } else return cartItems;

    }

    private String getValidDateFromTodayMessage(LocalDateTime validFrom) {
        if (validFrom.isAfter(LocalDateTime.now())) {
            long days = ChronoUnit.DAYS.between(LocalDateTime.now(), validFrom);
            long hours = ChronoUnit.HOURS.between(LocalDateTime.now().plusDays(days), validFrom);
            return VoucherMessage.VOUCHER_VALID_FROM_MESSAGE + days + " days and " + hours + " hours";
        }
        return null;
    }

    private boolean checkMinSpendReached(double minSpend, double subtotal) {
        return subtotal >= minSpend;
    }

    private String getMessage(boolean isProductVoucherApplicable,
                              String isValidDateFromTodayMessage,
                              boolean isMinSpendReached,
                              boolean isRedeemed) {
        log.info("In getMessage ");
        log.info(String.valueOf(isProductVoucherApplicable));
        log.info(String.valueOf(isValidDateFromTodayMessage));
        log.info(String.valueOf(isMinSpendReached));
        log.info(String.valueOf(isRedeemed));
        return isValidDateFromTodayMessage != null ? isValidDateFromTodayMessage :
                (!isProductVoucherApplicable ? VoucherMessage.PRODUCTS_SPECIFIC_MESSAGE :
                        (!isMinSpendReached ? VoucherMessage.SUBTOTAL_SMALLER_THAN_MIN_SPEND_MESSAGE :
                                (!isRedeemed ? VoucherMessage.VOUCHER_NOT_REDEEMED_MESSAGE : null
                                )
                        )
                );
    }

    @Override
    public boolean checkConstraintSatisfied(double minSpend,
                                            LocalDateTime validUntil,
                                            LocalDateTime validFrom,
                                            int usage,
                                            double subTotal) {
        log.info("Checking constraint");
        return minSpend <= subTotal &&
                validUntil.isAfter(LocalDateTime.now()) &&
                validFrom.isBefore(LocalDateTime.now()) &&
                usage > 0;
    }

    @Override
    public FSVoucherResponse getFreeShippingVouchers() {
        return null;
    }

    @Override
    public DCVoucherResponse getDiscountCashbackVouchers() {
        return null;
    }

    @Override
    @Transactional
    // locking resource
    public void redeemVoucher(String uuidVoucher) {
        String uuidRedeemUser = SecurityUtils.getCurrentUserUuid();
        voucherRedemptionService.saveShopVoucherRedemption(uuidVoucher, uuidRedeemUser);
    }

    @Override
    @Transactional
    public void updateVoucherUsage(Map<String, String> shopVouchers) {
        if(shopVouchers == null) return;
        List<String> uuidVouchers = shopVouchers.values().stream().toList();
        log.info("Update voucher usage {}", uuidVouchers);
        if (!uuidVouchers.isEmpty()) {
            List<VoucherRedemption> voucherRedemptions =
                    voucherRedemptionRepository.findByUuidVoucherInAndUuidUser(uuidVouchers, SecurityUtils.getCurrentUserUuid());
            if (voucherRedemptions.isEmpty()) throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
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
