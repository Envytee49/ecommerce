package org.example.ecommerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.common.constants.Status;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.dto.request.CancelOrderRequest;
import org.example.ecommerce.order.model.CancelledOrder;
import org.example.ecommerce.order.model.CancelledOrderReason;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.repository.CancelledOrderReasonRepository;
import org.example.ecommerce.order.repository.CancelledOrderRepository;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.order.service.CancelOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelOrderServiceImpl implements CancelOrderService {
    private final CancelledOrderRepository cancelledOrderRepository;
    private final CancelledOrderReasonRepository cancelledOrderReasonRepository;
    private final OrderRepository orderRepository;

    @Override
    public void cancelOrder(CancelOrderRequest request) {
        if (!cancelledOrderRepository.existsById(request.getUuidReason()))
            throw new AppException(ErrorCode.CANCEL_ORDER_REASON_NOT_EXIST);

        Order order = orderRepository.findById(request.getUuidOrder())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXIST));

        if (getNotAllowedToBeCancelledOrderStatus().contains(order.getStatus()))
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CANCELED);

        cancelledOrderRepository.save(CancelledOrder.builder()
                .uuidOrder(request.getUuidOrder())
                .uuidReason(request.getUuidReason())
                .status(Status.PENDING)
                .build());
    }

    @Override
    public List<CancelledOrderReason> getCancelledOrderReasons() {
        return cancelledOrderReasonRepository.findAll();
    }

    @Override
    public Object getCancelledOrderDetail(String uuidOrder) {
        return null;
    }

    private List<OrderStatus> getNotAllowedToBeCancelledOrderStatus() {
        return List.of(OrderStatus.CANCELLED, OrderStatus.ORDER_SHIPPED, OrderStatus.ORDER_DECLINED, OrderStatus.DELIVERED);
    }
}
