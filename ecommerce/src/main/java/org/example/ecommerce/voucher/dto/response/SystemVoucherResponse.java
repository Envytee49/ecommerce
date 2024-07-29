package org.example.ecommerce.voucher.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class SystemVoucherResponse {
    private Map<String, List<VoucherResponse>> systemVouchers;
}
