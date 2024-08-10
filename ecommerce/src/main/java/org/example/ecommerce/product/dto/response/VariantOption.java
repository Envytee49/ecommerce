package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Builder
@Setter
public class VariantOption {
    private String uuidProductVariantOption;
    private String option;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantOption that = (VariantOption) o;
        return Objects.equals(uuidProductVariantOption, that.uuidProductVariantOption);
    }

}
