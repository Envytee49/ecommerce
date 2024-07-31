package org.example.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.common.constants.PaymentMethod;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class PlaceOrderRequest {
    @NotBlank(message = "uuidAddress must not be blank")
    private String uuidUAddress;
    private String note;
    @NotNull(message = "paymentMethod must not be null")
    private PaymentMethod paymentMethod;
    @NotEmpty(message = "Please pick at least 1 cart item")
    private List<String> uuidCartItems;
    @NotNull(message = "shopVouchers must not be null")
    private Map<String, String> shopVouchers;
}
