package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.cart.dto.response.CartItemResponse;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.projection.CartItemProjection;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.response.*;
import org.example.ecommerce.order.service.DiscountService;
import org.example.ecommerce.order.service.InvoiceService;
import org.example.ecommerce.order.service.ShippingService;
import org.example.ecommerce.shop.model.Shop;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.user.model.DefaultUserAddress;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.user.repository.DefaultUserAddressRepository;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.user.service.UserAddressService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final ShopRepository shopRepository;
    private final CartRepository cartRepository;
    private final DiscountService discountService;
    private final ShippingService shippingService;
    private final UserAddressService userAddressService;
    @Override
    public InvoiceResponse getInvoice(InvoiceRequest request) {
        log.info("Invoice request: {}", request);

        // address
        String uuidUAddress = request.getUuidUAddress();
        DeliveryInfo deliveryInfo = DeliveryInfo.from(userAddressService.getUserAddress(uuidUAddress));
        // list of detail invoice for each shops - if there many
        List<InvoiceDetailResponse> invoiceDetailResponses =
                getInvoiceDetails(
                        request.getShopVouchers(),
                        request.getUuidCartItems(),
                        deliveryInfo.getUuidUAddress(),
                        request.getFreeShippingVoucher(),
                        request.getDiscountCashbackVoucher());

        TotalInvoice totalInvoice = new TotalInvoice();
        invoiceDetailResponses
                .forEach(invoice -> totalInvoice.updateInvoice(
                        invoice.getMerchandiseSubTotal(),
                        invoice.getShippingSubtotal(),
                        invoice.getShippingDiscountSubtotal(),
                        invoice.getVoucherDiscount(),
                        invoice.getTotalPayment()));

        // compute total discount when apply platform voucher
        return InvoiceResponse.builder()
                .deliveryInfo(deliveryInfo)
                .detailResponses(invoiceDetailResponses)
                .totalInvoice(totalInvoice)
                .build();
    } // cache invoice id

    public List<InvoiceDetailResponse> getInvoiceDetails(Map<String, String> shopVouchers,
                                                         List<String> uuidCartItems,
                                                         String uuidUAddress,
                                                         String freeShippingVoucher,
                                                         String discountCashbackVoucher) {
        log.info("getInvoiceDetails");
        log.info("find cart items");
        List<CartItemProjection> cartItems = cartRepository.findAllByUuidCartItemAndUuidCart(
                uuidCartItems,
                SecurityUtils.getCurrentUserCartUuid());

        if (cartItems.size() != uuidCartItems.size())
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);

        Map<String, List<CartItemResponse>> itemsByShop = extractItemsByShop(cartItems);

        List<InvoiceDetailResponse> invoiceDetailResponses = new ArrayList<>();
        // key: shopId - value: selected voucher
        log.info("find shops");
        List<Shop> shops = shopRepository.findAllById(itemsByShop.keySet().stream().toList());

        if (shops.size() != itemsByShop.size()) // check ?
            throw new AppException(ErrorCode.SHOP_NOT_FOUND);

        for (Shop shop : shops) {
            // Cart items logic
            String uuidShop = shop.getUuidShop();
            List<CartItemResponse> cartItemResponses = itemsByShop.get(uuidShop);
            List<CartItem> cartItemByShop = getCartItems(cartItemResponses);

            String seller = shop.getName();

            double merchandiseSubTotal = Utils.computeCartItemSubTotal(cartItemByShop);
            double shippingSubtotal = shippingService.computeShipping(uuidUAddress, uuidShop, cartItemByShop);
            double shippingDiscountSubtotal = discountService.computeShippingDiscount(
                    freeShippingVoucher,
                    uuidUAddress,
                    uuidShop,
                    shippingSubtotal,
                    cartItemByShop);
            double voucherDiscount = discountService.computeVoucherDiscount(
                    merchandiseSubTotal,
                    shopVouchers,
                    uuidShop,
                    discountCashbackVoucher,
                    cartItemByShop);
            double totalPayment = merchandiseSubTotal + shippingSubtotal - shippingDiscountSubtotal - voucherDiscount;

            invoiceDetailResponses.add(InvoiceDetailResponse.builder()
                    .merchandiseSubTotal(merchandiseSubTotal)
                    .shippingSubtotal(shippingSubtotal)
                    .shippingDiscountSubtotal(shippingDiscountSubtotal)
                    .voucherDiscount(voucherDiscount)
                    .totalPayment(totalPayment)
                    .shopName(seller)
                    .items(cartItemResponses)
                    .uuidShop(uuidShop)
                    .build());
        }
        return invoiceDetailResponses;
    }

    private Map<String, List<CartItemResponse>> extractItemsByShop(List<CartItemProjection> cartItems) {
        log.info("extractItemsByShop");

        Map<String, List<CartItemResponse>> itemsByShop = new HashMap<>();
        for (CartItemProjection cartItem : cartItems) {
            String shopUuid = cartItem.getUuidShop();
            // Check if the map already contains the shop's UUID
            if (!itemsByShop.containsKey(shopUuid)) {
                // If not, create a new list and add it to the map
                itemsByShop.put(shopUuid, new ArrayList<>());
            }
            // Add the cart item to the list corresponding to the shop's UUID
            itemsByShop.get(shopUuid).add(CartItemResponse.from(cartItem));
        }
        return itemsByShop;
    }

    private List<CartItem> getCartItems(List<CartItemResponse> cartItemResponse) {
        return cartItemResponse.stream().map(c -> CartItem
                .builder()
                        .uuidCartItem(c.getUuidCartItem())
                        .uuidProduct(c.getUuidProduct())
                        .uuidSku(c.getUuidSku())
                        .unitPrice(c.getUnitPrice())
                        .quantity(c.getQuantity())
                        .discount(c.getDiscountPercentage())
                .build())
                .toList();
    }
}
