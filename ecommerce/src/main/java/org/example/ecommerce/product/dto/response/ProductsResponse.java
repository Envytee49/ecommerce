package org.example.ecommerce.product.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductsResponse {
    private long total;
    private List<ProductResponse> products;
}
