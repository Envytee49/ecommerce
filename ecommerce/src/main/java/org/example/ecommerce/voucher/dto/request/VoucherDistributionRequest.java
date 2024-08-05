package org.example.ecommerce.voucher.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class VoucherDistributionRequest {
    @NotEmpty(message = "uuidVouchers must not be empty")
    private List<String> uuidVouchers;
}
