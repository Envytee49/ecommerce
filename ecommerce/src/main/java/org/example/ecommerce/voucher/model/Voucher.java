package org.example.ecommerce.voucher.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.constants.VoucherType;
import org.example.ecommerce.common.model.AbstractEntity;
import org.example.ecommerce.common.util.Utils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher extends AbstractEntity {
    @Id
    @Builder.Default
    @Size(max = 40)
    @Column(name = "uuid_voucher")
    private String uuidVoucher = Utils.getUuid();
    @Column(name = "voucher_name")
    @Size(max = 200)
    private String voucherName;
    @Column(name = "voucher_type")
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;
    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    @Column(name = "voucher_code")
    @Size(max = 20)
    private String voucherCode;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "discount")
    private Double discount; // 60k - 0.5
    @Column(name = "description")
    @Size(max = 200)
    private String description;
    @Column(name = "uuid_shop")
    private String uuidShop;
}
