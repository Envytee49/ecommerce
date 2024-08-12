package org.example.ecommerce.shop.repository;

import org.example.ecommerce.shop.model.Shop;
import org.example.ecommerce.shop.projection.TBProjection;
import org.example.ecommerce.shop.projection.TSPProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, String> {
    Shop findByUuidSeller(String uuidSeller);
    // order -> shop id = current seller shop id
    // order item ->
    // COUNT(quantity) group by uuidProduct
    @Query(value = "SELECT p.uuid_product as uuidProduct," +
            " p.title as productTitle," +
            " COUNT(oi.uuid_order) as orderCounts ," +
            " SUM(oi.quantity) as unitsSold," +
            " SUM(oi.price * oi.quantity * (1 - oi.discount)) as revenue " +
            "FROM product p " +
            "JOIN order_item oi ON oi.uuid_product = p.uuid_product " +
            "WHERE p.uuid_shop = :uuid_shop " +
            "GROUP BY p.uuid_product", nativeQuery = true)
    Page<TSPProjection> findTopSellingProducts(@Param("uuid_shop") String uuidShop, Pageable pageable);
    @Query(value = "SELECT u.uuid_user as uuidUser, " +
            "u.username as username, " +
            "SUM(o.total_payment) as totalSpend, " +
            "COUNT(o.uuid_order) as orderCounts " +
            "FROM `Order`  o " +
            "INNER JOIN User u ON o.uuid_user = u.uuid_user " +
            "WHERE o.uuid_shop = :uuidShop " +
            "GROUP BY u.uuid_user", nativeQuery = true)
    Page<TBProjection> findTopBuyers(@Param("uuidShop") String uuidShop, Pageable pageable);

    @Query(value = "SELECT s.name FROM Shop s WHERE s.name = :name")
    Optional<String> findByName(String name);
}
