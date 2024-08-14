package org.example.ecommerce.product.projections;

import java.time.LocalDateTime;

public interface ProductReviewProjection {
    String getUuidProductReview();
    String getUsername();
    String getComment();
    String getVariation();
    String getTitle();
    Integer getRating();
    LocalDateTime getCreatedDate();
}
