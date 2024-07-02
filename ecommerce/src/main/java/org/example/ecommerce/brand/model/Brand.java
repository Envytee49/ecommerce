package org.example.ecommerce.brand.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "brand")
public class Brand extends AbstractEntity {
    @Id
    @Column(name = "uuid_brand")
    @Builder.Default
    private String uuidBrand = Utils.getUuid();

    @Builder.Default
    @Column(name = "name")
    private String name = null;

    @NotNull
    @Column(name = "uuid_user")
    private String uuidUser;
}
