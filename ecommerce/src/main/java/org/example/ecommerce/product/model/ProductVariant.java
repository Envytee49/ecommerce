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
@Table(name = "product_variant")
public class ProductVariant {
    @Id
    @Size(max = 36)
    @Builder.Default
    @Column(name = "uuid_product_variant")
    private String uuidProductVariant = Utils.getUuid();
    @Column(name = "uuid_product")
    private String uuidProduct;
    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "ProductVariant{" +
                "uuidProductVariant='" + uuidProductVariant + '\'' +
                ", uuidProduct='" + uuidProduct + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}