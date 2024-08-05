package org.example.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class FetchOrderDetailRequest {
    @NotBlank(message = "uuidOrder must not be blank")
    private String uuidOrder;
    @NotBlank(message = "uuidShop must not be blank")
    private String uuidShop;
}
