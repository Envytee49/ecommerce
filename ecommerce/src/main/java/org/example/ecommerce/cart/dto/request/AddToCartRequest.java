package org.example.ecommerce.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    @NotBlank(message = "uuidProduct must not be blank")
    private String uuidProduct;
    // key: uuidProductVariant value: uuidProductVariantOption
    private List<ProductVariantRequest> productVariants;
    @Min(1)
    private int quantity = 1;
}
