package org.example.ecommerce.product.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.util.Utils;

@Entity
@Table(name = "product_attribute")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttribute {
    @Id
    @NotNull
    @Size(max = 40)
    @Builder.Default
    @Column(name = "uuid_attribute")
    private String uuidAttribute = Utils.getUuid();;

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct;

    @Size(max = 200)
    @Column(name = "value")
    private String value;
}

