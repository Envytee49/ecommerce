package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.UserRole;
import org.example.ecommerce.user.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUuidUser(String uuidUser);
}
