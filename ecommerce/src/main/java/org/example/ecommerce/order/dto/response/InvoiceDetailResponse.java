package org.example.ecommerce.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.cart.dto.response.CartItemResponse;

import java.util.List;
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceDetailResponse {
    private String shopName;
    private String uuidShop;
    private List<CartItemResponse> items;
    private Double merchandiseSubTotal;
    private Double shippingSubtotal;
    private Double shippingDiscountSubtotal;
    private Double voucherDiscount;
    private Double totalPayment;
}
