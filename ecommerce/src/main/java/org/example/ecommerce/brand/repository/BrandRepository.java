package org.example.ecommerce.brand.repository;

import org.example.ecommerce.brand.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, String> {
    Brand findByName(String name);
}
