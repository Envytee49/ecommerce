package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.ProductCategory;
import org.example.ecommerce.product.model.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
}
