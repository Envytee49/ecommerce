package org.example.ecommerce.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;

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


