package org.example.ecommerce.voucher.dto.request;

import lombok.Getter;

/**
 * CreateDiscountCashBackVoucherRequest
 * */
@Getter
public class CreatePlatformVoucherRequest extends CreateVoucherRequest {
    private String uuidCategory;
}
