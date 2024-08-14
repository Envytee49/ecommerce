package org.example.ecommerce.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(SkuProductVariantOptionId.class)
@Table(name = "sku_product_variant_option")
public class SkuProductVariantOption {
    @Id
    @Column(name = "uuid_sku")
    private String uuidSku;
    @Id
    @Column(name = "uuid_product_variant")
    private String uuidProductVariant;
    @Id
    @Column(name = "uuid_product_variant_option")
    private String uuidProductVariantOption;
}
