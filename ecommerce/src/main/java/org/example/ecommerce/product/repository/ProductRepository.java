package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.projections.ShopUuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByTitle(String title);
}
