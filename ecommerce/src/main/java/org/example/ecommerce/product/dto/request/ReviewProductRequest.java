package org.example.ecommerce.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ReviewProductRequest {
//    @NotBlank(message = "uuidOrder must not be blank")
//    private String uuidOrder;
    @NotBlank(message = "comment must not be blank")
    private String comment;
    @NotBlank(message = "title must not be blank")
    private String title;
    @Min(value = 1)
    @Max(value = 5)
    @NotNull(message = "Please give a rating")
    private Integer rating;
}
