package org.example.ecommerce.order.service;

import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.response.InvoiceResponse;

public interface InvoiceService {
    InvoiceResponse getInvoice(InvoiceRequest request);
}
