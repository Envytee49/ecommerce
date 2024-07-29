package org.example.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotBlank
    private String uuidProduct;

    @NotNull
    private Double unitPrice;

    @NotNull
    private Double discountPrice;

    @NotNull
    private Integer quantity;
}
