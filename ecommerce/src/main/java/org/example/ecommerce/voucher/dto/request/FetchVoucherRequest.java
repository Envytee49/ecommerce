package org.example.ecommerce.voucher.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
@Getter
public class FetchVoucherRequest {
    @NotBlank(message = "uuidShop must not be blank")
    private String uuidShop;
    @NotNull(message = "subTotal must not be null")
    private double subTotal;
    @NotEmpty(message = "uuidProducts must not be empty")
    private List<String> uuidProducts;
}
