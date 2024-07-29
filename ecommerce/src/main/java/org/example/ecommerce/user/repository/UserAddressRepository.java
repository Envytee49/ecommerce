package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, String> {
    List<UserAddress> findByUuidUser(String uuidUser);
}
