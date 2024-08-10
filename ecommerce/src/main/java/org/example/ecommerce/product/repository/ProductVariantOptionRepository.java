package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.ProductVariantOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantOptionRepository extends JpaRepository<ProductVariantOption, String> {
}
