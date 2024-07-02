package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = "select c from Category c " +
            "where c.uuidCategory in " +
            "(select pc.uuidCategory " +
            "from ProductCategory pc " +
            "where pc.uuidProduct = :uuidProduct)")
    List<Category> findByUuidProduct(@Param("uuidProduct") String uuidProduct);
}
