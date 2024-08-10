package org.example.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    @NotEmpty(message = "Please pick at least 1 cart item")
    private List<String> uuidCartItems;
    private Map<String, String> shopVouchers;
    private String discountCashbackVoucher;
    private String freeShippingVoucher;
    private String uuidUAddress;
}
