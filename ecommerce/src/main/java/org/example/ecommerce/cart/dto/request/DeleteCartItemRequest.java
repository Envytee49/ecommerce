package org.example.ecommerce.cart.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartItemRequest {
    @NotBlank(message = "uuidProduct must not be blank")
    private String uuidProduct;
    @NotBlank(message = "uuidProduct must not be blank")
    private String uuidCart;
}
