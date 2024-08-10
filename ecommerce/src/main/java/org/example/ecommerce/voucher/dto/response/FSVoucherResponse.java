package org.example.ecommerce.voucher.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FSVoucherResponse {
    private List<VoucherResponse> shopVouchers;
}
