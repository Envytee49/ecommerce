package org.example.ecommerce.voucher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRedemptionId implements Serializable {
    private String uuidVoucher;
    private String uuidUser;
}