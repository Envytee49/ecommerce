package org.example.ecommerce.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.order.dto.request.CancelOrderRequest;
import org.example.ecommerce.order.service.CancelOrderService;
import org.example.ecommerce.order.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class CancelOrderController extends AbstractController {
    private final CancelOrderService orderService;

    @GetMapping("/cancel-reason")
    public ApiResponse<?> getCancelOrderReason() {
        return respond(() -> orderService.getCancelledOrderReasons());
    }

    @GetMapping("/history/cancelled/{uuidOrder}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> getCancelledOrderDetail(@PathVariable String uuidOrder) {
        return respond(() -> orderService.getCancelledOrderDetail(uuidOrder));
    }

    @PutMapping("/cancel")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> cancelOrder(@RequestBody @Valid CancelOrderRequest request) {
        return respond(() -> orderService.cancelOrder(request), "Order cancelled");
    }
}
