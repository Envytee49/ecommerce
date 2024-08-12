package org.example.ecommerce.cart.dto.request;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ProductVariantRequest {
    private String uuidProductVariant;
    private String uuidProductVariantOption;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductVariantRequest that = (ProductVariantRequest) o;
        return Objects.equals(uuidProductVariant, that.uuidProductVariant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuidProductVariant);
    }
}
