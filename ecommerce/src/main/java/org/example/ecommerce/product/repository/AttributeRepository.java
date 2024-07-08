package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, String> {
    @Query(value = "SELECT pa.value AS value, a.key AS key " +
            "FROM ProductAttribute pa " +
            "INNER JOIN Attribute a ON a.uuidAttribute = pa.uuidAttribute " +
            "WHERE pa.uuidProduct = :uuidProduct")
    // nested sql jpa
    // jdbc template
    // interface
    List<IAttribute> findByUuidProduct(@Param("uuidProduct") String uuidProduct);
}
