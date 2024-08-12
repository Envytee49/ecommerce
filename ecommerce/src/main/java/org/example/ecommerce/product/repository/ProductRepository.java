package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.projections.ProductResponseProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Product findByTitle(String title);

    Optional<Product> findByUuidProductAndUuidShop(String uuidProduct, String uuidShop);

    @Query("SELECT " +
            " p.uuidProduct as uuidProduct, " +
            " p.title as title, " +
            " p.price as price," +
            " p.discount as discount," +
            " SUM(oi.quantity) as sold, " +
            " AVG(pv.rating) as averageRating " +
            " FROM Product p " +
            "LEFT JOIN ProductReview pv ON p.uuidProduct = pv.uuidProduct " +
            "LEFT JOIN OrderItem oi ON oi.uuidProduct = p.uuidProduct " +
            "WHERE p.uuidProduct in (:uuidProducts) " +
            "GROUP BY p.uuidProduct")
    List<ProductResponseProjection> findAll(List<String> uuidProducts);
}
