package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, String> {
    Brand findByName(String name);
}
