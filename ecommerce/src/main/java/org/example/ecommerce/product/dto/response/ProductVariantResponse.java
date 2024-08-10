package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Builder
@Setter
public class ProductVariantResponse {
    private String uuidProductVariant;
    private String productVariant;
    private List<VariantOption> variantOptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductVariantResponse that = (ProductVariantResponse) o;
        return Objects.equals(uuidProductVariant, that.uuidProductVariant);
    }
    public void updateProductVariantOptions(VariantOption variantOption) {
        this.variantOptions.add(variantOption);
    }
}
