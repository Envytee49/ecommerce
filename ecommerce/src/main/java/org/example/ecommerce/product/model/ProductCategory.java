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
public class ProductCategory {
    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "uuid_product")
    private Product product;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "uuid_category")
    private Category category;
}


