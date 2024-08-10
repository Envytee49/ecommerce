package org.example.ecommerce.voucher.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ecommerce.common.constants.DiscountType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateVoucherRequest {
    @NotNull
    private DiscountType discountType;
    @NotBlank
    @Size(min = 5, max = 12)
    private String voucherCode;
    @Min(value = 1)
    @NotNull
    private Integer quantity;
    @Min(value = 1)
    private Double discountValue;
    @Min(value = 0)
    private Integer discountPercentage;
    @NotNull
    @Min(value = 1)
    private Double discountCap;
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
