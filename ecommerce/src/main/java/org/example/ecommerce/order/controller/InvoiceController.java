package org.example.ecommerce.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.response.InvoiceResponse;
import org.example.ecommerce.order.service.InvoiceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController extends AbstractController {
    private final InvoiceService invoiceService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<InvoiceResponse> getInvoice(@Valid @RequestBody InvoiceRequest request) {
        return this.respond(() -> invoiceService.getInvoice(request));
    }
}
