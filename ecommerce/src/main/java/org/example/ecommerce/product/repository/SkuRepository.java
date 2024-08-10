package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SkuRepository extends JpaRepository<Sku, String> {
    @Query(value = "SELECT s " +
            "FROM Sku s " +
            "WHERE s.uuidSku = ( " +
            "    SELECT spvo.uuidSku  " +
            "    FROM SkuProductVariantOption spvo " +
            "    WHERE spvo.uuidProductVariant IN :productVariants " +
            "    AND spvo.uuidProductVariantOption IN :productVariantOptions " +
            "    GROUP BY spvo.uuidSku " +
            "    ORDER BY COUNT(spvo.uuidSku) " +
            "    LIMIT 1" +
            ") AND s.uuidProduct = :uuidProduct")
    Optional<Sku> findByVariantOption(String uuidProduct, List<String> productVariants, List<String> productVariantOptions);
}
