package org.example.ecommerce.product.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ProductsResponse {
    private long total;
    private List<ProductResponse> products;

    public ProductsResponse(long total, List<ProductResponse> products) {
        this.total = total;
        this.products = products;
    }
}
