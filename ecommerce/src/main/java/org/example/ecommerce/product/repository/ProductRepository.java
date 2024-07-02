package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByTitle(String title);
}
