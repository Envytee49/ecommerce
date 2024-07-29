package org.example.ecommerce.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartRequest {
    @NotBlank(message = "uuidCartItem must not be blank")
    private String uuidCartItem;
    @Min(1)
    private int quantity = 1;
}
