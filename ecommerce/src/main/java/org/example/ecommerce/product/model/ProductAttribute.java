package org.example.ecommerce.product.model;


import jakarta.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "uuid_product")
    private Product product;
    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "uuid_attribute")
    private Attribute attribute;

    @Size(max = 200)
    @Column(name = "_value")
    private String value;
}

