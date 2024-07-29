package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, String> {
    Shop findByUuidSeller(String uuidSeller);
}
