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
import org.example.ecommerce.model.AbstractEntity;
import org.example.ecommerce.util.Utils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`order`")
public class Order extends AbstractEntity {
    @Id
    @Column(name = "uuid_order")
    @Builder.Default
    private String uuidOrder = Utils.getUuid();

    @Builder.Default
    @Size(max = 40)
    @Column(name = "user_id")
    private String userId = null;

    @NotNull
    @Size(max = 100)
    @Column(name = "session_id")
    @Builder.Default
    private String sessionId = Utils.getUuid();

    @NotNull
    @Size(max = 100)
    @Column(name = "token")
    @Builder.Default
    private String token = Utils.getUuid();

    @NotNull
    @Builder.Default
    @Column(name = "status")
    private int status = 0;

    @NotNull
    @Builder.Default
    @Column(name = "subtotal")
    private double subtotal = 0;

    @NotNull
    @Builder.Default
    @Column(name = "item_discount")
    private double itemDiscount = 0;

    @NotNull
    @Builder.Default
    @Column(name = "tax")
    private double tax = 0;

    @NotNull
    @Builder.Default
    @Column(name = "shipping")
    private double shipping = 0;

    @NotNull
    @Builder.Default
    @Column(name = "total")
    private double total = 0;

    @Builder.Default
    @Size(max = 50)
    @Column(name = "promo")
    private String promo = null;

    @NotNull
    @Builder.Default
    @Column(name = "discount")
    private double discount = 0;

    @NotNull
    @Builder.Default
    @Column(name = "grand_total")
    private double grandTotal = 0;

    @Builder.Default
    @Size(max = 20)
    @Column(name = "phone")
    private String phone = null;

    @Builder.Default
    @Size(max = 45)
    @Column(name = "payment_methods")
    private String paymentMethods = null;

    @Column(name = "note")
    private String note;

    @Builder.Default
    @Size(max = 1000)
    @Column(name = "address_ship")
    private String addressShip = null;

    @Builder.Default
    @Size(max = 40)
    @Column(name = "seller_id")
    private String sellerId = null;
}
