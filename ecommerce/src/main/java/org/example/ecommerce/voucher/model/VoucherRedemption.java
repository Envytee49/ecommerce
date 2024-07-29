package org.example.ecommerce.voucher.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher_redemption")
@IdClass(VoucherRedemptionId.class)
public class VoucherRedemption {
    @Id
    @Size(max = 40)
    @Column(name = "uuid_voucher")
    private String uuidVoucher;

    @Id
    @Size(max = 40)
    @Column(name = "uuid_user")
    private String uuidUser;

    @Column(name = "`usage`")
    private int usage;

    @Column(name = "redemption_date")
    private LocalDateTime redemptionDate;

    @PrePersist
    protected void onCreate() {
        redemptionDate = LocalDateTime.now();
    }
}