package org.example.ecommerce.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductCreateRequest {
    @NotBlank
    private String title;

    private String metaTitle;

    private String summary;

    @NotNull
    private int type;

    @NotNull
    private double price;

    @NotNull
    private int quantity;

    private String description;

    private LocalDateTime publishedDate;
}
