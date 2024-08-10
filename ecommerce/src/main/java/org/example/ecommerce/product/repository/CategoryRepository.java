package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.dto.response.CategoryResponse;
import org.example.ecommerce.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Category findByTitle(String title);
    @Query(value =
            "SELECT new org.example.ecommerce.product.dto.response.CategoryResponse(c.uuidCategory, c.title) " +
                    "FROM Category c INNER JOIN ProductCategory pc ON c.uuidCategory = pc.uuidCategory " +
                    "INNER JOIN Product p ON pc.uuidProduct = p.uuidProduct " +
                    "WHERE p.uuidProduct = :uuidProduct")
    List<CategoryResponse> findByUuidProduct(@Param("uuidProduct") String uuidProduct);
}
