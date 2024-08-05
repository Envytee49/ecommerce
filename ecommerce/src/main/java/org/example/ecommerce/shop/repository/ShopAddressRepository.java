package org.example.ecommerce.shop.repository;

import org.example.ecommerce.shop.model.ShopAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopAddressRepository extends JpaRepository<ShopAddress, String> {
}
