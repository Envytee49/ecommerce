package org.example.ecommerce.voucher.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "voucher_info")
public class VoucherInfo extends AbstractEntity {
    @Id
    @Size(max = 40)
    @Column(name = "uuid_voucher_info")
    @Builder.Default
    private String uuidVoucherInfo = Utils.getUuid();
    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    @Column(name = "voucher_code")
    @Size(max = 20)
    private String voucherCode;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "discount_value")
    private Double discountValue; // 60k - 0.5@
    @Column(name = "discount_percentage")
    private Double discountPercentage; // 60k - 0.5
    @Column(name = "discount_cap")
    private Double discountCap; // fixed
    @Column(name = "description")
    @Size(max = 200)
    private String description;
    @Column(name = "is_visible")
    @NotNull
    private boolean isVisible;
}
