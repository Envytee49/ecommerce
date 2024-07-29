package org.example.ecommerce.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.request.PlaceOrderRequest;
import org.example.ecommerce.order.dto.response.InvoiceResponse;
import org.example.ecommerce.order.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController extends AbstractController {
    private final OrderService orderService;
    @GetMapping("/invoice")
    public ApiResponse<InvoiceResponse> getInvoice(@Valid @RequestBody InvoiceRequest request) {
        return this.respond(() -> orderService.getInvoice(request));
    }
    @PostMapping("/place-order")
    public ApiResponse<?> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return respond(() -> orderService.placeOrder(request), "Order placed");
    }
    @PostMapping("/cancel/{uuidOrder}")
    public ApiResponse<?> cancelOrder(@PathVariable String uuidOrder) {
        return respond(() -> orderService.cancelOrder(uuidOrder), "Order cancelled");
    }

    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @PostMapping("/decline/{uuidOrder}")
    public ApiResponse<?> declineOrder(@PathVariable String uuidOrder) {
        return respond(() -> orderService.declineOrder(uuidOrder), "Order declined");
    }
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @PostMapping("/approve/{uuidOrder}")
    public ApiResponse<?> approveOrder(@PathVariable String uuidOrder) {
        return respond(() -> orderService.approveOrder(uuidOrder), "Order approved");
    }
}
