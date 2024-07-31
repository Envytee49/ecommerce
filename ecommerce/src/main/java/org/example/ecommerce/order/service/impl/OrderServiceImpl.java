package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.cart.service.CartService;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.dto.request.PlaceOrderRequest;
import org.example.ecommerce.order.dto.response.InvoiceDetailResponse;
import org.example.ecommerce.order.dto.response.TotalInvoice;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.model.OrderItem;
import org.example.ecommerce.order.repository.OrderItemRepository;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.order.service.OrderService;
import org.example.ecommerce.user.repository.UserAddressRepository;
import org.example.ecommerce.voucher.model.VoucherRedemption;
import org.example.ecommerce.voucher.repository.VoucherRedemptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserAddressRepository userAddressRepository;
    private final OrderItemRepository orderItemRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    private final InvoiceServiceImpl invoiceService;
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
        if (cartItems.isEmpty())
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);

        // amounts
        List<InvoiceDetailResponse> invoiceDetailResponses = invoiceService.getInvoiceDetails(request.getShopVouchers(), cartItems);

        TotalInvoice totalInvoice = new TotalInvoice();
        invoiceDetailResponses
                .forEach(invoice -> totalInvoice.updateInvoice(
                        invoice.getShipping(),
                        invoice.getSubTotal(),
                        invoice.getDiscount(),
                        invoice.getTotalAmount()));

        // check if voucher is valid
        List<String> uuidVouchers =
                request.getShopVouchers().values().stream().toList();
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
        Order order = Order.builder()
                .subtotal(totalInvoice.getSubTotal())
                .shipping(totalInvoice.getShipping())
                .total(totalInvoice.getTotalAmount())
                .discount(totalInvoice.getDiscount())
                .note(request.getNote())
                .uuidUAddress(request.getUuidUAddress())
                .userId(SecurityUtils.getCurrentUserUuid())
                .status(OrderStatus.ORDER_PLACED)
                .paymentMethod(request.getPaymentMethod())
                .build();

        List<OrderItem> orderItems = cartItems
                .stream()
                .map(cartItem -> OrderItem.builder()
                        .uuidOrder(order.getUuidOrder())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getUnitPrice())
                        .discount(cartItem.getDiscount())
                        .uuidProduct(cartItem.getUuidProduct())
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


}
