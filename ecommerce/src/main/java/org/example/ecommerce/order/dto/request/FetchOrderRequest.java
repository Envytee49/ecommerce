package org.example.ecommerce.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.OrderStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOrderRequest {
    @NotNull
    private OrderStatus status;
}
