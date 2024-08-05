package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.response.*;
import org.example.ecommerce.order.dto.response.CartItemResponse;
import org.example.ecommerce.order.dto.response.InvoiceDetailResponse;
import org.example.ecommerce.order.dto.response.InvoiceResponse;
import org.example.ecommerce.order.service.InvoiceService;
import org.example.ecommerce.shop.model.Shop;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.voucher.model.Voucher;
import org.example.ecommerce.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final VoucherRepository voucherRepository;
    private final ShopRepository shopRepository;
    private final CartRepository cartRepository;
    private final UserAddressRepository userAddressRepository;
    @Override
    public InvoiceResponse getInvoice(InvoiceRequest request) {
        // These are selected cart items
        List<String> uuidCartItems = request.getUuidCartItems();
        List<CartItem> cartItems = cartRepository.findAllById(uuidCartItems);
        if (cartItems.isEmpty())
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);

        // list of detail invoice for each shops - if there many
        List<InvoiceDetailResponse> invoiceDetailResponses = getInvoiceDetails(request.getShopVouchers(), cartItems);

        TotalInvoice totalInvoice = new TotalInvoice();
        invoiceDetailResponses
                .forEach(invoice -> totalInvoice.updateInvoice(
                        invoice.getShipping(),
                        invoice.getSubTotal(),
                        invoice.getDiscount(),
                        invoice.getTotalAmount()));
        // address
        String uuidUAddress = request.getUuidUAddress();
        DeliveryInfo deliveryInfo = getDeliveryInfo(uuidUAddress);

        return InvoiceResponse.builder()
                .deliveryInfo(deliveryInfo)
                .detailResponses(invoiceDetailResponses)
                .totalInvoice(totalInvoice)
                .build();
    } // cache invoice id
    public List<InvoiceDetailResponse> getInvoiceDetails(Map<String, String> shopVouchers, List<CartItem> cartItems) {

        Map<String, List<CartItem>> itemsByShop = extractItemsByShop(cartItems);

        List<InvoiceDetailResponse> invoiceDetailResponses = new ArrayList<>();
        // key: shopId - value: selected voucher
        List<Shop> shops = shopRepository.findAllById(itemsByShop.keySet().stream().toList());

        if(shops.size() != itemsByShop.size()) // check ?
            throw new AppException(ErrorCode.SHOP_NOT_FOUND);

        for (Shop shop : shops) {
            // Cart items logic
            String uuidShop = shop.getUuidShop();
            List<CartItem> cartItemByShop = itemsByShop.get(uuidShop);
            List<CartItemResponse> cartItemResponse = cartItemByShop
                    .stream().map(CartItemResponse::from).toList();

            String seller = shop.getName();

            double shipping = computeShipping();
            double subTotal = computeSubTotal(cartItemByShop);
            double discount = computeDiscount(subTotal, shopVouchers, uuidShop);
            double totalAmount = subTotal + shipping - discount;

            invoiceDetailResponses.add(InvoiceDetailResponse.builder()
                    .shipping(shipping)
                    .discount(discount)
                    .totalAmount(totalAmount)
                    .subTotal(subTotal)
                    .shopName(seller)
                    .items(cartItemResponse)
                    .uuidShop(uuidShop)
                    .build());
        }
        return invoiceDetailResponses;
    }
    private Map<String, List<CartItem>> extractItemsByShop(List<CartItem> cartItems) {
        Map<String, List<CartItem>> itemsByShop = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            String shopUuid = cartItem.getUuidShop();
            // Check if the map already contains the shop's UUID
            if (!itemsByShop.containsKey(shopUuid)) {
                // If not, create a new list and add it to the map
                itemsByShop.put(shopUuid, new ArrayList<CartItem>());
            }
            // Add the cart item to the list corresponding to the shop's UUID
            itemsByShop.get(shopUuid).add(cartItem);
        }
        return itemsByShop;
    }

    private double computeDiscount(double subTotal,
                                   Map<String, String> shopVouchers, String uuidShop) {
        if (shopVouchers.get(uuidShop) != null) {
            Voucher voucher = voucherRepository.findById(shopVouchers.get(uuidShop))
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
            return voucher.getDiscountType().getDiscount(voucher.getDiscount(), subTotal); // rely on client or get from database
        }
        return 0;
    }

    private DeliveryInfo getDeliveryInfo(String uuidUAddress) {
        if (StringUtils.isEmpty(uuidUAddress)) return null;
        UserAddress userAddress = userAddressRepository.findById(uuidUAddress).
                orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        return DeliveryInfo.from(userAddress);
    }

    private double computeSubTotal(List<CartItem> cartItems) {
        return cartItems
                .stream()
                .mapToDouble(
                        cartItem -> cartItem.getQuantity() * cartItem.getUnitPrice()).sum();
    }

    private double computeShipping() {
        return 200;
    }
}
