package org.example.ecommerce.voucher.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;
@Getter
public class FetchVoucherRequest {
    @NotBlank(message = "uuidShop must not be blank")
    private String uuidShop;
    private List<String> uuidCartItems;
}
