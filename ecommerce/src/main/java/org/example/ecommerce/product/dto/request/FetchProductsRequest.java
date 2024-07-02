package org.example.ecommerce.product.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FetchProductsRequest {
    @Min(0)
    private int page;
    @Min(20)
    private int size;
}
