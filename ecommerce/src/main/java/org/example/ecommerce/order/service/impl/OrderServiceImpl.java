package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.dto.response.CartItemResponse;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.aop.PlaceOrderAOP;
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
import org.example.ecommerce.product.service.ProductManagementService;
import org.example.ecommerce.product.service.impl.ProductManagementServiceImpl;
import org.example.ecommerce.user.model.UserAddress;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.user.service.UserInfoService;
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
    private final OrderRepository orderRepository;
    private final UserAddressRepository userAddressRepository;
    private final OrderItemRepository orderItemRepository;
    private final InvoiceService invoiceService;
    private final VoucherService voucherService;
    private final UserInfoService userInfoService;
    private final PlaceOrderAOP placeOrderAOP;
    private final ProductManagementService productManagementService;

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
        voucherService.updateVoucherUsage(request.getShopVouchers());
        //
        List<Order> orders = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        for (InvoiceDetailResponse invoiceDetailResponse : invoiceDetailResponses) {
            // check stock
            List<String> orderingItems = invoiceDetailResponse
                    .getItems()
                    .stream()
                    .map(CartItemResponse::getUuidCartItem)
                    .toList();
            placeOrderAOP.checkProductStock(orderingItems);

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
        // get only needed data and transform to cart item
        List<CartItem> updatedCartItems = invoiceDetailResponses.stream()
                .flatMap(i -> i.getItems().stream())
                .map(ci -> CartItem
                        .builder()
                        .uuidSku(ci.getUuidSku())
                        .uuidProduct(ci.getUuidProduct())
                        .quantity(ci.getQuantity())
                        .build())
                .toList();
        productManagementService.updateProductStock(updatedCartItems);
        orderRepository.saveAll(orders);
        orderItemRepository.saveAll(orderItems);
    }

    @Override
    public OrderHistoryResponse getOrderHistory(FetchOrderRequest request) {
        log.info("Get order history");
        log.info("Query order item projections");
        List<OrderItemProjection> orderItemProjections = orderRepository
                .findOrderHistory(SecurityUtils.getCurrentUserUuid(), request.getStatus().name());
        log.info("order item projections, {}", orderItemProjections.size());
        List<ShopOrder> orderItems = new ArrayList<>();
        log.info("Filter item by shop");
        for (OrderItemProjection o : orderItemProjections) {
            ShopOrder shopOrder = ShopOrder.builder()
                    .uuidOrder(o.getUuidOrder())
                    .uuidShop(o.getUuidShop())
                    .build();
            if (!orderItems.contains(shopOrder)) {
                shopOrder.setShopName(o.getShopName());
                shopOrder.setShippingSubtotal(o.getShippingSubtotal());
                shopOrder.setShippingDiscountSubtotal(o.getShippingDiscountSubtotal());
                shopOrder.setTotalPayment(o.getTotalPayment());
                shopOrder.setOrderItems(new ArrayList<>());
                orderItems.add(shopOrder);
            }
            orderItems.get(orderItems.indexOf(shopOrder)).updateOrderItemList(
                    OrderItemResponse.builder()
                            .uuidOrderItem(o.getUuidOrderItem())
                            .uuidProduct(o.getUuidProduct())
                            .uuidSku(o.getUuidSku())
                            .variant(o.getProductVariantOption())
                            .title(o.getProductTitle())
                            .quantity(o.getQuantity())
                            .price(o.getOriginalPrice())
                            .discount(o.getDiscountPercentage())
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
        if (orderItemProjections.isEmpty())
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);

        UserAddress userAddress = userAddressRepository.findById(orderItemProjections.get(0).getUuidUAddress())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        DeliveryInfo deliveryInfo = DeliveryInfo.from(userAddress);
        List<OrderItemResponse> orderItems = orderItemProjections
                .stream()
                .map(o -> OrderItemResponse
                        .builder()
                        .uuidOrderItem(o.getUuidOrderItem())
                        .uuidSku(o.getUuidSku())
                        .variant(o.getProductVariantOption())
                        .title(o.getProductTitle())
                        .quantity(o.getQuantity())
                        .price(o.getOriginalPrice())
                        .discount(o.getDiscountPercentage())
                        .uuidProduct(o.getUuidProduct())
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
