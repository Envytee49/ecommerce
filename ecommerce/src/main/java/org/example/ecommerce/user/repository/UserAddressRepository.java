package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, String> {
    List<UserAddress> findByUuidUser(String uuidUser);

    Optional<UserAddress> findByUuidUAddressAndUuidUser(String uuidUAddress, String uuidUser);
}
