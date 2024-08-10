package org.example.ecommerce.user.repository;

import org.example.ecommerce.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.uuidUser FROM User u " +
            "INNER JOIN UserRole ur ON ur.uuidUser = u.uuidUser AND ur.role = 'USER' ")
    List<String> getAllUuids();
}
