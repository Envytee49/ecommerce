package org.example.ecommerce.cart.model;


import jakarta.persistence.*;
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
    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_cart_item")
    @Builder.Default
    private String uuidCartItem = Utils.getUuid();

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_cart")
    private String uuidCart;

    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct;

    @Column(name = "uuid_sku")
    private String uuidSku;

    @Builder.Default
    @NotNull
    @Column(name = "price")
    private double unitPrice = 0;

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

    @Override
    public String toString() {
        return "CartItem{" +
                "unitPrice=" + unitPrice +
                ", uuidCartItem='" + uuidCartItem + '\'' +
                ", quantity=" + quantity +
                ", discount=" + discount +
                '}';
    }
}

