package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.service.CartService;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.request.PlaceOrderRequest;
import org.example.ecommerce.order.dto.response.*;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.model.OrderItem;
import org.example.ecommerce.order.repository.OrderItemRepository;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.order.service.OrderService;
import org.example.ecommerce.user.model.Shop;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.user.repository.ShopRepository;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.voucher.model.Voucher;
import org.example.ecommerce.voucher.model.VoucherRedemption;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartService cartService;
    private final VoucherRepository voucherRepository;
    private final OrderRepository orderRepository;
    private final UserAddressRepository userAddressRepository;
    private final ShopRepository shopRepository;
    private final OrderItemRepository orderItemRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;

    @Override
    public InvoiceResponse getInvoice(InvoiceRequest request) {
        // These are selected cart items
        List<String> uuidCartItems = request.getUuidCartItems();
        List<CartItem> cartItems = cartService.getSelectedCartItems(uuidCartItems);
        if (cartItems.isEmpty())
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);

        // list of detail invoice for each shops - if there many
        List<InvoiceDetailResponse> detailResponses = new ArrayList<>();


        Map<String, List<CartItem>> itemsByShop = extractItemsByShop(cartItems);

        TotalInvoice totalInvoice = new TotalInvoice();
        // key: shopId - value: selected voucher
        Map<String, String> shopVouchers = request.getShopVouchers();

        for (Map.Entry<String, List<CartItem>> shopItems : itemsByShop.entrySet()) {
            // Cart items logic
            String uuidShop = shopItems.getKey();
            List<CartItem> cartItemByShop = shopItems.getValue();
            List<OrderItemResponse> orderItemResponse = cartItemByShop
                    .stream().map(OrderItemResponse::from).toList();

            Shop shop = shopRepository.findById(uuidShop).
                    orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_FOUND
                    ));
            String seller = shop.getName();

            double shipping = computeShipping();
            double subTotal = computeSubTotal(cartItemByShop);
            double promotion = computePromotion(subTotal, shipping, shopVouchers, uuidShop);
            double totalAmount = subTotal + shipping - promotion;

            totalInvoice.updateInvoice(shipping, subTotal, promotion, totalAmount);
            detailResponses.add(
                    InvoiceDetailResponse.builder()
                            .items(orderItemResponse)
                            .uuidShop(uuidShop)
                            .promotion(promotion)
                            .subTotal(subTotal)
                            .shipping(shipping)
                            .totalAmount(totalAmount)
                            .seller(seller)
                            .build()
            );
        }
        // address
        String uuidUAddress = request.getUuidUAddress();
        DeliveryInfo deliveryInfo = getDeliveryInfo(uuidUAddress);

        return InvoiceResponse.builder()
                .deliveryInfo(deliveryInfo)
                .detailResponses(detailResponses)
                .totalInvoice(totalInvoice)
                .build();
    }

    @Override
    @Transactional
    public void placeOrder(PlaceOrderRequest request) {
        // check address existed
        String uuidAddress = request.getUuidUAddress();
        if (userAddressRepository.findById(uuidAddress).isEmpty())
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        // amounts
        double shipping = request.getShipping();
        double subTotal = request.getSubTotal();
        double totalAmount = request.getTotalAmount();
        double discount = request.getPromotion();

        // check if voucher is valid
        if (!request.getUuidVouchers().isEmpty()) {
            List<VoucherRedemption> voucherRedemptions =
                    voucherRedemptionRepository.findByUuidVoucherIn(request.getUuidVouchers());
            // should be the same size - if not client passes wrong values
            if (voucherRedemptions.size() != request.getUuidVouchers().size())
                throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);

            for (VoucherRedemption voucherRedemption : voucherRedemptions) {
                // if max usage is reached
                if (voucherRedemption.getUsage() - 1 < 0)
                    throw new AppException(ErrorCode.MAX_VOUCHER_USAGE_REACHED);
                voucherRedemption.setUsage(voucherRedemption.getUsage() - 1);
                voucherRedemptionRepository.save(voucherRedemption);
            }
        }
        Order order = Order.builder()
                .subtotal(subTotal)
                .shipping(shipping)
                .total(totalAmount)
                .discount(discount)
                .note(request.getNote())
                .uuidUAddress(request.getUuidUAddress())
                .userId(SecurityUtils.getCurrentUserUuid())
                .status(OrderStatus.ORDER_PLACED)
                .paymentMethod(request.getPaymentMethod())
                .build();

        List<OrderItem> orderItems = request
                .getOrderItems()
                .stream()
                .map(orderItemRequest -> OrderItem.builder()
                        .uuidOrder(order.getUuidOrder())
                        .quantity(orderItemRequest.getQuantity())
                        .price(orderItemRequest.getUnitPrice())
                        .discount(orderItemRequest.getDiscountPrice())
                        .uuidProduct(orderItemRequest.getUuidProduct())
                        .build()
                )
                .toList();

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
    }

    @Override
    public void cancelOrder(String uuidOrder) {

    }

    @Override
    public void declineOrder(String uuidOrder) {

    }

    @Override
    public void approveOrder(String uuidOrder) {

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

    private double computePromotion(double subTotal, double discount,
                                    Map<String, String> shopVouchers, String uuidShop) {
        if (shopVouchers.get(uuidShop) != null) {
            Voucher voucher = voucherRepository.findById(shopVouchers.get(uuidShop))
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
            return voucher.getDiscountType().getDiscount(discount, subTotal); // rely on client or get from database
        }
        return 0;
    }

    private DeliveryInfo getDeliveryInfo(String uuidUAddress) {
        if (StringUtils.isEmpty(uuidUAddress)) return null;
        UserAddress userAddress = userAddressRepository.findById(uuidUAddress).
                orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        return DeliveryInfo.builder()
                .mobile(userAddress.getMobile())
                .receiver(userAddress.getReceiverName())
                .city(userAddress.getCity())
                .district(userAddress.getDistrict())
                .street(userAddress.getStreet())
                .postalCode(userAddress.getPostalCode())
                .build();
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
