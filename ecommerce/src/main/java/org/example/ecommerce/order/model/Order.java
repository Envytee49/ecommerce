package org.example.ecommerce.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.common.constants.PaymentMethod;
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;

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

    @Size(max = 40)
    @NotNull
    @Column(name = "uuid_user")
    private String uuidUser;

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_shop")
    @JsonIgnore
    private String uuidShop;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @NotNull
    @Builder.Default
    @Column(name = "merchandise_subtotal")
    private double merchandiseSubtotal = 0;

    @NotNull
    @Builder.Default
    @Column(name = "shipping_subtotal")
    private double shippingSubtotal = 0;

    @NotNull
    @Builder.Default
    @Column(name = "shipping_discount_subtotal")
    private double shippingDiscountSubtotal = 0;

    @NotNull
    @Builder.Default
    @Column(name = "voucherDiscount")
    private double voucherDiscount = 0;

    @NotNull
    @Builder.Default
    @Column(name = "total_payment")
    private double totalPayment = 0;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "note")
    private String note;

    @NotNull
    @Size(max = 40)
    @Column(name = "uuid_uaddress")
    private String uuidUAddress;

}
