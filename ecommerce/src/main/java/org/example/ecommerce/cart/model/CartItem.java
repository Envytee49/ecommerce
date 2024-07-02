package org.example.ecommerce.cart.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem extends AbstractEntity {
    @Id
    @Column(name = "uuid_cart_item")
    @Builder.Default
    private String uuidCartItem = Utils.getUuid();

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_cart")
    private String uuidCart;

    @Builder.Default
    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct = null;

    @Builder.Default
    @NotNull
    @Column(name = "price")
    private double price = 0;

    @Builder.Default
    @NotNull
    @Column(name = "discount")
    private double discount = 0;

    @Builder.Default
    @NotNull
    @Column(name = "quantity")
    private int quantity = 0;

    @Builder.Default
    @NotNull
    @Column(name = "active")
    private int active = 0;

    @Column(name = "content")
    private String content;

}

