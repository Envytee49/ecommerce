package org.example.ecommerce.voucher.dto.response;

import lombok.*;
import org.example.ecommerce.common.constants.VoucherType;

import java.time.LocalDateTime;
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Getter
public class VoucherResponse {
    private String uuidVoucher;
    private String voucherName;
    private double discount;
    private String description;
    private VoucherType voucherType;
    private double minSpend;
    private int usage;
    private int maxUsage;
    private LocalDateTime validUntil;
    private LocalDateTime validFrom;
    private boolean isApplicable;

    public VoucherResponse(String uuidVoucher,
                           String voucherName,
                           double discount,
                           String description,
                           VoucherType voucherType,
                           double minSpend,
                           int maxUsage,
                           LocalDateTime validUntil,
                           LocalDateTime validFrom) {
        this.uuidVoucher = uuidVoucher;
        this.voucherName = voucherName;
        this.discount = discount;
        this.description = description;
        this.voucherType = voucherType;
        this.minSpend = minSpend;
        this.maxUsage = maxUsage;
        this.validUntil = validUntil;
        this.validFrom = validFrom;
    }
    public VoucherResponse(String uuidVoucher,
                           String voucherName,
                           double discount,
                           String description,
                           VoucherType voucherType,
                           double minSpend,
                           int usage,
                           int maxUsage,
                           LocalDateTime validUntil,
                           LocalDateTime validFrom) {
        this.uuidVoucher = uuidVoucher;
        this.voucherName = voucherName;
        this.discount = discount;
        this.description = description;
        this.voucherType = voucherType;
        this.minSpend = minSpend;
        this.usage = usage;
        this.maxUsage = maxUsage;
        this.validUntil = validUntil;
        this.validFrom = validFrom;
    }
    public VoucherResponse(String uuidVoucher,
                           String voucherName,
                           double discount,
                           String description,
                           VoucherType voucherType,
                           double minSpend,
                           int maxUsage,
                           LocalDateTime validUntil,
                           LocalDateTime validFrom,
                           boolean isApplicable) {
        this(uuidVoucher, voucherName, discount, description, voucherType, minSpend, maxUsage, validUntil, validFrom);
        this.isApplicable = isApplicable;
    }

}
