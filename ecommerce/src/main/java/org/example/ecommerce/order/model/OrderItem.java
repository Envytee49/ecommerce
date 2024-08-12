package org.example.ecommerce.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;
import org.example.ecommerce.product.model.Product;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem extends AbstractEntity {
    @Id
    @NotNull
    @Column(name = "uuid_order_item")
    @Builder.Default
    @JsonIgnore
    private String uuidOrderItem = Utils.getUuid();

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct;

    @Column(name = "uuid_sku")
    private String uuidSku;

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_order")
    @JsonIgnore
    private String uuidOrder;

    @NotNull
    @Builder.Default
    @Column(name = "price")
    private double price = 0;

    @NotNull
    @Builder.Default
    @Column(name = "discount")
    private double discount = 0;

    @NotNull
    @Builder.Default
    @Column(name = "quantity")
    private double quantity = 0;

}
