package org.example.ecommerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.order.service.OrderManagementService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/management")
@RequiredArgsConstructor
public class OrderManagementController extends AbstractController {
    private final OrderManagementService orderService;

    @PutMapping("/decline/{uuidOrder}")
    public ApiResponse<?> declineOrder(@PathVariable String uuidOrder) {
        return respond(() -> orderService.declineOrder(uuidOrder), "Order declined");
    }

    @PutMapping("/approve/{uuidOrder}")
    public ApiResponse<?> approveOrder(@PathVariable String uuidOrder) {
        return respond(() -> orderService.approveOrder(uuidOrder), "Order approved");
    }

}
