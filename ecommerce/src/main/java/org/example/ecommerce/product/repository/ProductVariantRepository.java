package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.ProductVariant;
import org.example.ecommerce.product.projections.ProductVariantProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    @Query(value = "SELECT pv.name as productVariant, " +
            "pv.uuidProductVariant as uuidProductVariant, " +
            "pvo.name as variantOption, " +
            "pvo.uuidProductVariantOption as uuidProductVariantOption " +
            "FROM ProductVariant pv " +
            "INNER JOIN ProductVariantOption pvo ON pv.uuidProductVariant = pvo.uuidProductVariant " +
            "WHERE pv.uuidProduct = :uuidProduct")
    List<ProductVariantProjection> findByUuidProduct(String uuidProduct);

    @Query(value = "SELECT COUNT(p.uuid_Product_Variant) FROM Product_Variant p " +
            "WHERE p.uuid_Product = :uuidProduct", nativeQuery = true)
    int countProductVariant(String uuidProduct);

}
