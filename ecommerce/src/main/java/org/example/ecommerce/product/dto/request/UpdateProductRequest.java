package org.example.ecommerce.product.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UpdateProductRequest {
    private String uuidProduct;

    @Min(value = 0, message = "price should be equal or more than 0")
    private Double price;

    @Min(value = 0, message = "quantity should be equal or more than 0")
    private Integer quantity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedDate;
}
