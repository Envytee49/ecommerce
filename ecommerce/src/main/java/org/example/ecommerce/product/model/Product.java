package org.example.ecommerce.product.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.model.AbstractEntity;
import org.example.ecommerce.util.Utils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends AbstractEntity {
    @Id
    @Size(max = 40)
    @Builder.Default
    @Column(name = "uuid_product")
    private String uuidProduct = Utils.getUuid();

    @NotNull
    @Size(max = 75)
    @Column(name = "title")
    private String title;

    @Size(max = 100)
    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "summary")
    private String summary;

    @NotNull
    @Builder.Default
    @Column(name = "type")
    private int type = 0;

    @NotNull
    @Builder.Default
    @Column(name = "price")
    private double price = 0;

    @NotNull
    @Builder.Default
    @Column(name = "quantity")
    private int quantity = 0;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "description")
    private String description;

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_brand")
    private String uuidBrand;
}

