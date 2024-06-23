package org.example.ecommerce.product.repository;

import org.example.ecommerce.product.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, String> {
    @Query(value = "select a from Attribute a \n" +
            "where a.uuidAttribute in \n" +
            "(select pa.uuidAttribute \n" +
            "from ProductAttribute pa\n" +
            " where pa.uuidProduct = :uuidProduct)")
    List<Attribute> findByUuidAttribute(@Param("uuidProduct") String uuidProduct);
}
