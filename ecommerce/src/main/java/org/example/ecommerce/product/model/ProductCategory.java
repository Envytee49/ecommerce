package org.example.ecommerce.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProductCategoryId.class)
public class ProductCategory {
    @Id
    @NotNull
    @Column(name = "uuid_product")
    private String uuidProduct;

    @Id
    @NotNull
    @Column(name = "uuid_category")
    private String uuidCategory;
}


