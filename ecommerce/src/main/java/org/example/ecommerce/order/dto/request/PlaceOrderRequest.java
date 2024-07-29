package org.example.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.example.ecommerce.common.constants.PaymentMethod;

import java.util.List;

@Getter
@Builder
public class PlaceOrderRequest {
    @NotNull(message = "uuidAddress must not be null")
    private String uuidUAddress;
    private String note;
    @NotNull(message = "paymentMethod must not be null")
    private PaymentMethod paymentMethod;
    @NotEmpty(message = "Please pick at least 1 cart item")
    private List<OrderItemRequest> orderItems;
    @NotNull
    private List<String> uuidVouchers;
    @NotNull
    private Double subTotal;
    @NotNull
    private Double totalAmount;
    @NotNull
    private Double promotion;
    @NotNull
    private Double shipping;
}
