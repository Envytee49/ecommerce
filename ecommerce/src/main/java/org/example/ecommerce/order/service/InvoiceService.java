package org.example.ecommerce.order.service;

import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.order.dto.request.InvoiceRequest;
import org.example.ecommerce.order.dto.response.InvoiceDetailResponse;
import org.example.ecommerce.order.dto.response.InvoiceResponse;

import java.util.List;
import java.util.Map;

public interface InvoiceService {
    InvoiceResponse getInvoice(InvoiceRequest request);

    List<InvoiceDetailResponse> getInvoiceDetails(Map<String, String> shopVouchers,
                                                  List<String> uuidCartItems,
                                                  String uuidUAddress,
                                                  String freeShippingVoucher,
                                                  String discountCashbackVoucher);
}
