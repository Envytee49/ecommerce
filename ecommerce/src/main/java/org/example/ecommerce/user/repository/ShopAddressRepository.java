package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.ShopAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopAddressRepository extends JpaRepository<ShopAddress, String> {
}
