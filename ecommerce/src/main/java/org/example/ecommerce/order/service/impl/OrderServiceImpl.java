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
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.model.OrderItem;
import org.example.ecommerce.order.projection.OrderItemProjection;
import org.example.ecommerce.order.repository.OrderItemRepository;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.order.service.InvoiceService;
import org.example.ecommerce.order.service.OrderService;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.user.service.UserInfoService;
import org.example.ecommerce.user.service.impl.UserInfoServiceImpl;
import org.example.ecommerce.voucher.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserAddressRepository userAddressRepository;
    private final OrderItemRepository orderItemRepository;
    private final InvoiceService invoiceService;
    private final VoucherService voucherService;
    private final UserInfoService userInfoService;

    @Override
    @Transactional
    public void placeOrder(PlaceOrderRequest request) {
        // check address existed
        String uuidUAddress = request.getUuidUAddress();
        if (userAddressRepository.findByUuidUAddressAndUuidUser(uuidUAddress, SecurityUtils.getCurrentUserUuid()).isEmpty())
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        // amounts
        List<InvoiceDetailResponse> invoiceDetailResponses =
                invoiceService.getInvoiceDetails(request.getShopVouchers(),
                        request.getUuidCartItems(),
                        request.getUuidUAddress(),
                        request.getFreeShippingVoucher(),
                        request.getDiscountCashbackVoucher());
        // update voucher usage
        List<String> uuidVouchers =
                request.getShopVouchers().values().stream().toList();
        voucherService.updateVoucherUsage(uuidVouchers);

        //
        List<Order> orders = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        for(InvoiceDetailResponse invoiceDetailResponse : invoiceDetailResponses) {
            Order order = Order.builder()
                    .merchandiseSubtotal(invoiceDetailResponse.getMerchandiseSubTotal())
                    .shippingSubtotal(invoiceDetailResponse.getShippingSubtotal())
                    .shippingDiscountSubtotal(invoiceDetailResponse.getShippingDiscountSubtotal())
                    .voucherDiscount(invoiceDetailResponse.getVoucherDiscount())
                    .totalPayment(invoiceDetailResponse.getTotalPayment())
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
                            .discount(cartItem.getDiscountPercentage())
                            .uuidProduct(cartItem.getUuidProduct())
                            .uuidSku(cartItem.getUuidSku())
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
        for(OrderItemProjection o : orderItemProjections) {
            ShopOrder shopOrder = ShopOrder.builder()
                    .uuidOrder(o.getUuidOrder())
                    .uuidShop(o.getUuidShop())
                    .build();
            if(!orderItems.contains(shopOrder)) {
                shopOrder.setShopName(o.getShopName());
                shopOrder.setShippingSubtotal(o.getShippingSubtotal());
                shopOrder.setShippingDiscountSubtotal(o.getShippingDiscountSubtotal());
                shopOrder.setTotalPayment(o.getTotalPayment());
                shopOrder.setOrderItems(new ArrayList<>());
                orderItems.add(shopOrder);
            }
            orderItems.get(orderItems.indexOf(shopOrder)).updateOrderItemList(OrderItem.builder()
                            .quantity(o.getQuantity())
                            .price(o.getOriginalPrice())
                            .discount(o.getDiscountPercentage())
                            .uuidProduct(o.getUuidProduct())
                    .build());
        }
        return OrderHistoryResponse.builder().orderItems(orderItems).build();
    }

    @Override
    public OrderItemDetailResponse getOrderDetails(FetchOrderDetailRequest request) {
        List<OrderItemProjection> orderItemProjections =
                orderRepository.findOrderDetail(request.getUuidOrder(),
                        request.getUuidShop(),
                        SecurityUtils.getCurrentUserUuid());
        if(orderItemProjections.isEmpty())
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);

        UserAddress userAddress = userAddressRepository.findById(orderItemProjections.get(0).getUuidUAddress())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        DeliveryInfo deliveryInfo = DeliveryInfo.from(userAddress);
        List<OrderItem> orderItems = orderItemProjections
                .stream()
                .map(orderItemProjection -> OrderItem
                        .builder()
                        .quantity(orderItemProjection.getQuantity())
                        .price(orderItemProjection.getOriginalPrice())
                        .discount(orderItemProjection.getDiscountPercentage())
                        .uuidProduct(orderItemProjection.getUuidProduct())
                        .build())
                .toList();
        OrderItemProjection orderItem = orderItemProjections.get(0);
        ShopOrder shopOrder = ShopOrder.builder()
                .uuidOrder(orderItem.getUuidOrder())
                .uuidShop(orderItem.getUuidShop())
                .merchandiseSubtotal(orderItem.getMerchandiseSubtotal())
                .shippingSubtotal(orderItem.getShippingSubtotal())
                .shippingDiscountSubtotal(orderItem.getShippingDiscountSubtotal())
                .voucherDiscount(orderItem.getVoucherDiscount())
                .totalPayment(orderItem.getTotalPayment())
                .shopName(orderItem.getShopName())
                .orderItems(orderItems)
                .build();

        return OrderItemDetailResponse.builder()
                .deliveryInfo(deliveryInfo)
                .orderItem(shopOrder).build();
    }

    @Override
    @Transactional
    public void changeAddress(String uuidUAddress) {
        userInfoService.changeDefaultAddress(uuidUAddress);
    }

}
