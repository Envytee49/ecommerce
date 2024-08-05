package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.dto.request.FetchOrderDetailRequest;
import org.example.ecommerce.order.dto.request.FetchOrderRequest;
import org.example.ecommerce.order.dto.request.PlaceOrderRequest;
import org.example.ecommerce.order.dto.response.*;
import org.example.ecommerce.order.dto.response.InvoiceDetailResponse;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.model.OrderItem;
import org.example.ecommerce.cart.projection.CartItemProjection;
import org.example.ecommerce.order.projection.OrderItemProjection;
import org.example.ecommerce.order.repository.OrderItemRepository;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.order.service.InvoiceService;
import org.example.ecommerce.order.service.OrderService;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.voucher.model.VoucherRedemption;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.example.ecommerce.voucher.service.VoucherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserAddressRepository userAddressRepository;
    private final OrderItemRepository orderItemRepository;
    private final InvoiceService invoiceService;
    private final VoucherService voucherService;
    @Override
    @Transactional
    public void placeOrder(PlaceOrderRequest request) {
        // check address existed
        String uuidAddress = request.getUuidUAddress();
        if (userAddressRepository.findById(uuidAddress).isEmpty())
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        // selected cart items
        List<String> uuidCartItems = request.getUuidCartItems();
        List<CartItem> cartItems = cartRepository.findAllById(uuidCartItems);
        if (cartItems.size() != uuidCartItems.size())
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);

        // amounts
        List<InvoiceDetailResponse> invoiceDetailResponses =
                invoiceService.getInvoiceDetails(request.getShopVouchers(), cartItems);

        // update voucher usage
        List<String> uuidVouchers =
                request.getShopVouchers().values().stream().toList();
        voucherService.updateVoucherUsage(uuidVouchers);

        //
        List<Order> orders = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        for(InvoiceDetailResponse invoiceDetailResponse : invoiceDetailResponses) {
            Order order = Order.builder()
                    .subtotal(invoiceDetailResponse.getSubTotal())
                    .shipping(invoiceDetailResponse.getShipping())
                    .total(invoiceDetailResponse.getTotalAmount())
                    .discount(invoiceDetailResponse.getDiscount())
                    .uuidShop(invoiceDetailResponse.getUuidShop())
                    .note(request.getNote())
                    .uuidUAddress(request.getUuidUAddress())
                    .uuidUser(SecurityUtils.getCurrentUserUuid())
                    .status(OrderStatus.ORDER_PLACED)
                    .paymentMethod(request.getPaymentMethod())
                    .build();
            orders.add(order);

            orderItems.addAll(invoiceDetailResponse.getItems()
                    .stream()
                    .map(cartItem -> OrderItem.builder()
                            .uuidOrder(order.getUuidOrder())
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getUnitPrice())
                            .discount(cartItem.getDiscountPrice())
                            .uuidProduct(cartItem.getUuidProduct())
                            .build()
                    )
                    .toList());
        }

        orderRepository.saveAll(orders);
        orderItemRepository.saveAll(orderItems);
    }

    @Override
    public OrderHistoryResponse getOrderHistory(FetchOrderRequest request) {
        List<OrderItemProjection> orderItemProjections = orderRepository
                .findOrderHistory(SecurityUtils.getCurrentUserUuid(), request.getStatus());
        List<ShopOrder> orderItems = new ArrayList<>();
        for(OrderItemProjection orderItemProjection : orderItemProjections) {
            ShopOrder shopOrder = ShopOrder.builder()
                    .uuidOrder(orderItemProjection.getUuidOrder())
                    .uuidShop(orderItemProjection.getUuidShop())
                    .build();
            if(!orderItems.contains(shopOrder)) {
                shopOrder.setShopName(orderItemProjection.getShopName());
                shopOrder.setShippingFee(orderItemProjection.getShippingFee());
                shopOrder.setTotalPrice(orderItemProjection.getTotalPrice());
                shopOrder.setOrderItems(new ArrayList<>());
                orderItems.add(shopOrder);
            }
            orderItems.get(orderItems.indexOf(shopOrder)).updateOrderItemList(OrderItem.builder()
                            .quantity(orderItemProjection.getQuantity())
                            .price(orderItemProjection.getOriginalPrice())
                            .discount(orderItemProjection.getDiscountPrice())
                            .uuidProduct(orderItemProjection.getUuidProduct())
                    .build());
        }
        return OrderHistoryResponse.builder().orderItems(orderItems).build();
    }

    @Override
    public OrderItemDetailResponse getOrderDetails(FetchOrderDetailRequest request) {
        List<OrderItemProjection> orderItemProjections =
                orderRepository.findOrderDetail(request.getUuidOrder(), request.getUuidShop());
        UserAddress userAddress = userAddressRepository.findById(orderItemProjections.get(0).getUuidUAddress())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        DeliveryInfo deliveryInfo = DeliveryInfo.from(userAddress);
        List<OrderItem> orderItems = orderItemProjections
                .stream()
                .map(orderItemProjection -> OrderItem
                        .builder()
                        .quantity(orderItemProjection.getQuantity())
                        .price(orderItemProjection.getOriginalPrice())
                        .discount(orderItemProjection.getDiscountPrice())
                        .uuidProduct(orderItemProjection.getUuidProduct())
                        .build())
                .toList();
        ShopOrder shopOrder = ShopOrder.builder()
                .uuidOrder(orderItemProjections.get(0).getUuidOrder())
                .uuidShop(orderItemProjections.get(0).getUuidShop())
                .shippingFee(orderItemProjections.get(0).getShippingFee())
                .totalPrice(orderItemProjections.get(0).getTotalPrice())
                .shopName(orderItemProjections.get(0).getShopName())
                .orderItems(orderItems)
                .build();

        return OrderItemDetailResponse.builder()
                .deliveryInfo(deliveryInfo)
                .orderItem(shopOrder).build();
    }

}
