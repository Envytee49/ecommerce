package org.example.ecommerce.voucher.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.util.Utils;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher_constraint")
public class VoucherConstraint {
    @Id
    @Size(max = 40)
    @Column(name = "uuid_voucher")
    private String uuidVoucher;
    @Column(name = "minimum_spend")
    private double minSpend;
    @Column(name = "max_usage")
    private int maxUsage;
    @Column(name = "valid_until")
    private LocalDateTime validUntil;
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
}
