package org.example.ecommerce.voucher.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.constants.VoucherType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateVoucherRequest {
    @NotBlank
    private String voucherName;
    @NotNull
    private DiscountType discountType;
    @NotBlank
    @Size(min = 5, max = 12)
    private String voucherCode;
    @Min(value = 1)
    @NotNull
    private Integer quantity;
    @Min(value = 0)
    @NotNull
    private Double discount;
    @NotBlank
    private String description;
    @Min(value = 1)
    @NotNull
    private Double minSpend;
    @NotNull
    private LocalDateTime validFrom;
    @NotNull
    private LocalDateTime validUntil;
    @NotNull
    @Min(value = 1)
    private Integer maxUsage;
    @NotNull
    private Boolean isVisible;
}
