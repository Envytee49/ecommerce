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
import org.example.ecommerce.util.Utils;

@Entity
@Table(name = "product_category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    @Id
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct;

    @Id
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_category")
    private String uuidCategory;
}


