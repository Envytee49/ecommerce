package org.example.ecommerce.voucher.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.constants.ShopVoucherType;
import org.example.ecommerce.voucher.projection.VoucherResponseProjection;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherResponse {
    private String uuidVoucher;
    private String voucherName;
    private Double discountValue;
    private Double discountPercentage;
    private String description;
    private ShopVoucherType shopVoucherType;
    private Double minSpend;
    private Integer remainingUsage;
    private Integer maxUsage;
    private LocalDateTime validUntil;
    private LocalDateTime validFrom;
    private boolean isRedeemed;
    private boolean isApplicable;
    private String message;

    public static VoucherResponse from(VoucherResponseProjection v) {
        return VoucherResponse.builder()
                .uuidVoucher(v.getUuidVoucher())
                .voucherName(getVoucherName(v))
                .discountValue(v.getDiscountValue())
                .discountPercentage(v.getDiscountPercentage())
                .description(v.getDescription())
                .shopVoucherType(v.getShopVoucherType())
                .minSpend(v.getMinSpend())
                .remainingUsage(v.getRemainingUsage())
                .maxUsage(v.getMaxUsage())
                .validUntil(v.getValidUntil())
                .validFrom(v.getValidFrom())
                .isRedeemed(v.getIsRedeemed())
                .isApplicable(false)
                .build();
    }

    private static String getVoucherName(VoucherResponseProjection v) {
        StringBuilder discountMessage = new StringBuilder();
        if (v.getDiscountType() == DiscountType.PERCENTAGE) {
            discountMessage
                    .append(v.getDiscountPercentage()*100)
                    .append("% Off Up To ")
                    .append(v.getDiscountCap())
                    .append("$");
        } else {
            discountMessage.append(v.getDiscountValue()).append("$ Off");
        }
        return discountMessage.toString();
    }
}
