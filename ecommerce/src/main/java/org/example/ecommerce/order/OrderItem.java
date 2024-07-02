package org.example.ecommerce.order;

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
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;

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
    private String uuidOrderItem = Utils.getUuid();

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct;

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_order")
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
    private int quantity = 0;

    @Column(name = "content")
    private String content;
}
