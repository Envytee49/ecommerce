package org.example.ecommerce.product.helper;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
@Builder
@Getter
public class VariantMap {
    // key: name - value : uuid
    private String uuidProductVariant;
    private String uuidProductVariantOption;
}
