package org.example.ecommerce.product.model;


import lombok.Getter;

import java.io.Serializable;
@Getter
public class SkuProductVariantOptionId implements Serializable {
    private String uuidSku;
    private String uuidProductVariant;
    private String uuidProductVariantOption;
}
