package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.SkuProductVariantOption;
import org.example.ecommerce.product.model.SkuProductVariantOptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuProductVariantOptionRepository extends JpaRepository<SkuProductVariantOption, SkuProductVariantOptionId> {
}
