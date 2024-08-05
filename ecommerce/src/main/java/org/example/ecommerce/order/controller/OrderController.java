package org.example.ecommerce.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.order.dto.request.FetchOrderDetailRequest;
import org.example.ecommerce.order.dto.request.FetchOrderRequest;
import org.example.ecommerce.order.dto.request.PlaceOrderRequest;
import org.example.ecommerce.order.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController extends AbstractController {
    private final OrderService orderService;

    @PostMapping("/place-order")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return respond(() -> orderService.placeOrder(request), "Order placed");
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> getOrderHistory(@RequestBody @Valid FetchOrderRequest request) {
        return respond(() -> orderService.getOrderHistory(request));
    }

    @GetMapping("/history/details")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> getOrderDetails(@RequestBody @Valid FetchOrderDetailRequest request) {
        return respond(() -> orderService.getOrderDetails(request));
    }


}
