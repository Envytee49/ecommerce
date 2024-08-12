package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {
}
