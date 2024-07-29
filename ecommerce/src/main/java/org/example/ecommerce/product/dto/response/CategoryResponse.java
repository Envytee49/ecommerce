package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryResponse {
    private String uuidCategory;
    private String title;
}
