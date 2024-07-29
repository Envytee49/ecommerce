package org.example.ecommerce.voucher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductVoucherRequest extends CreateVoucherRequest{
    private List<String> uuidProducts;
}
