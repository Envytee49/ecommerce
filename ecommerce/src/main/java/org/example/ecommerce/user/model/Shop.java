package org.example.ecommerce.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.product.model.ProductAttribute;
import org.example.ecommerce.product.model.ProductCategory;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shop")
public class Shop extends AbstractEntity {
    @Id
    @Size(max = 40)
    @Builder.Default
    @Column(name = "uuid_shop")
    private String uuidShop = Utils.getUuid();

    @NotNull
    @Size(max = 75)
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "uuid_seller")
    private String uuidSeller;
}


