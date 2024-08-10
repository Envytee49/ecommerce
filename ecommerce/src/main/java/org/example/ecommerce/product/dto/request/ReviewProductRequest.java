package org.example.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewProductRequest {
    @NotBlank(message = "uuidOrder must not be blank")
    private String uuidOrder;
    @NotBlank(message = "comment must not be blank")
    private String comment;
    @NotBlank(message = "title must not be blank")
    private String title;
    @Size(min = 1, max = 5)
    @NotNull(message = "Please give a rating")
    private Integer rating;
}
