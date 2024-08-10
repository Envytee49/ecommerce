package org.example.ecommerce.voucher.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.PlatformVoucherType;
import org.example.ecommerce.common.util.Utils;

@Table(name = "platform_voucher")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PlatformVoucher{
    @Id
    @Size(max = 40)
    @Builder.Default
    @Column(name = "uuid_voucher")
    private String uuidVoucher = Utils.getUuid();
    @Column(name = "uuid_voucher_info")
    private String uuidVoucherInfo;
    @Column(name = "uuid_voucher_constraint")
    private String uuidVoucherConstraint;
    @Column(name = "uuid_category")
    private String uuidCategory;
    @Column(name = "voucher_type")
    @Enumerated(EnumType.STRING)
    private PlatformVoucherType platformVoucherType;
}
