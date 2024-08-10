package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.DefaultUserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefaultUserAddressRepository extends JpaRepository<DefaultUserAddress, String> {
}
