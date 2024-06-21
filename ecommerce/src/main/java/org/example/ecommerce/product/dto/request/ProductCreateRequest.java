package org.example.ecommerce.product.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedDate;

    @NotBlank
    private String uuidBrand;

    @Override
    public String toString() {
        return "ProductCreateRequest{" +
                "title='" + title + '\'' +
                ", metaTitle='" + metaTitle + '\'' +
                ", summary='" + summary + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", publishedDate=" + publishedDate +
                ", uuidBrand='" + uuidBrand + '\'' +
                '}';
    }
}
