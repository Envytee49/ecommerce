package org.example.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.example.ecommerce.cart.dto.request.ProductVariantRequest;

import java.util.Set;

@Getter
public class ProductVariantDetailRequest {
    @NotBlank(message = "uuidProduct must not be blank")
    private String uuidProduct;
    @NotEmpty
    private Set<ProductVariantRequest> productVariants;
}
