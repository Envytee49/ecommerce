package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.projections.ShopUuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByTitle(String title);

    Optional<Product> findByUuidProductAndUuidShop(String uuidProduct, String uuidShop);
}
