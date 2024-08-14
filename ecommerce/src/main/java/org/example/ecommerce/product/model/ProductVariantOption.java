package org.example.ecommerce.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.util.Utils;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_variant_option")
public class ProductVariantOption {
    @Id
    @Size(max = 36)
    @Builder.Default
    @Column(name = "uuid_product_variant_option")
    private String uuidProductVariantOption = Utils.getUuid();
    @Column(name = "uuid_product_variant")
    @Size(max = 40)
    private String uuidProductVariant;
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "ProductVariantOption{" +
                "uuidProductVariantOption='" + uuidProductVariantOption + '\'' +
                ", uuidProductVariant='" + uuidProductVariant + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}