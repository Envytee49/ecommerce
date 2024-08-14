package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.ProductReview;
import org.example.ecommerce.product.projections.ProductReviewProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {
    @Query(value = "SELECT " +
            "pr.uuidProductReview as uuidProductReview, " +
            "pr.variation as variation, " +
            "pr.title as title, " +
            "pr.rating as rating, " +
            "pr.comment as comment, " +
            "pr.createdDate as createdDate, " +
            "u.username as username " +
            "FROM ProductReview pr " +
            "JOIN User u ON u.uuidUser = pr.uuidUser " +
            "WHERE pr.uuidProduct = :uuidProduct")
    Page<ProductReviewProjection> findProductReview(String uuidProduct, Pageable pageRequest);
    @Query(value = "SELECT avg(pr.rating) " +
            "FROM ProductReview pr " +
            "WHERE pr.uuidProduct = :uuidProduct " +
            "GROUP BY pr.uuidProduct")
    Double calculateAverageRating(String uuidProduct);
}
