package org.example.ecommerce.product.repository.custom;

import org.example.ecommerce.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchProductRepository {
    Page<ProductResponse> findAllWithFilters(
            String keyword, String sortBy, String sortDirection,
            Double minPrice, Double maxPrice, Integer ratingFilter,
            Pageable pageable);
}
