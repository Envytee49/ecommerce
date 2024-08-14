package org.example.ecommerce.product.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductReviewDetailResponse {
    private String uuidProductReview;
    private String username;
    private String comment;
    private String variation;
    private String title;
    private Integer rating;
    private LocalDateTime createdAt;
}
