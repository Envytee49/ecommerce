package org.example.ecommerce.product.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    private int quantity;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime publishedDate;

}
