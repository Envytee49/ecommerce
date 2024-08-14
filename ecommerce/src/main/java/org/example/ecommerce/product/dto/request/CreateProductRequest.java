package org.example.ecommerce.product.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "title must not be blank")
    private String title;

    private String metaTitle;

    private String summary;

    @NotNull
    @Min(value = 1)
    private Double price;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime publishedDate;

    private List<ProductVariantRequest> variants;

    /**
     * Only allow 2 variant at most
     * Example: "Red" : {
     *     "XXL" : {
     *         "sku" : RX,
     *         "price" : 120.00,
     *         "quantity" : 5,
     *     }
     * }
     */
    private Map<String, List<Map<String, SkuRequest>>> skus;

}
