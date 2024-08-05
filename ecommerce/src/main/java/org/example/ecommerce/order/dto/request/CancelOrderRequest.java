package org.example.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CancelOrderRequest {
    @NotBlank(message = "uuidOrder must not be blank")
    private String uuidOrder;
    @NotBlank(message = "uuidReason must not be blank")
    private String uuidReason;
}
