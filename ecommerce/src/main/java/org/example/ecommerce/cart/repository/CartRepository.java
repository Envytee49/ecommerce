package org.example.ecommerce.cart.repository;

import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.projection.CartItemProjection;
import org.example.ecommerce.order.projection.InvoiceItemProjection;
import org.example.ecommerce.order.projection.OrderItemStockProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, String> {
    @Query(value = "SELECT ci.uuid_Product as uuidProduct, " +
            "ci.uuid_Sku as uuidSku, " +
            "ci.uuid_Cart_Item as uuidCartItem, " +
            "ci.discount as discount, " +
            "ci.active as active, " +
            "ci.quantity as quantity, " +
            "ci.price as unitPrice, " +
            "p.title as title, " +
            "(SELECT GROUP_CONCAT(pvo.name SEPARATOR ', ')  " +
            "FROM sku_product_variant_option spvo " +
            "JOIN product_variant_option pvo ON spvo.uuid_product_variant_option = pvo.uuid_product_variant_option " +
            "WHERE spvo.uuid_sku = ci.uuid_Sku " +
            "GROUP BY spvo.uuid_sku ) AS productVariantOptions, " +
            "s.name as shopName, " +
            "s.uuid_Shop as uuidShop " +
            "FROM Cart_Item ci " +
            "INNER JOIN Product p ON ci.uuid_Product = p.uuid_Product " +
            "INNER JOIN Shop s ON p.uuid_Shop = s.uuid_Shop " +
            "WHERE ci.uuid_Cart = :uuidCart", nativeQuery = true)
    Page<CartItemProjection> findCartItemAndShop(String uuidCart, Pageable pageable);
    CartItem findByUuidCartAndUuidProduct(String uuidCart, String uuidProduct);
    void deleteByUuidCart(String uuidCart);

    @Query(value = "SELECT ci FROM CartItem ci " +
            "INNER JOIN Product p ON p.uuidProduct = ci.uuidProduct " +
            "INNER join ProductCategory pc ON pc.uuidProduct = p.uuidProduct " +
            "INNER JOIN Category c ON pc.uuidCategory = c.uuidCategory " +
            "WHERE ci.uuidCartItem in (:uuidCartItems) AND c.uuidCategory = :uuidCategory")
    List<CartItem> findCategoryMatchingItems(@Param("uuidCartItems") List<String> uuidCartItems,
                                             @Param("uuidCategory") String uuidCategory);
    @Query(value = "SELECT ci as cartItem, s.uuidShop as uuidShop " +
            "FROM CartItem ci " +
            "INNER JOIN Product p ON ci.uuidProduct = p.uuidProduct " +
            "INNER JOIN Shop s ON p.uuidShop = s.uuidShop " +
            "WHERE ci.uuidCartItem in (:uuidCartItems)")
    List<InvoiceItemProjection> findCartItemAndShop(List<String> uuidCartItems);

    @Query(value = "SELECT ci as cartItem " +
            "FROM CartItem ci " +
            "INNER JOIN Product p ON ci.uuidProduct = p.uuidProduct " +
            "WHERE ci.uuidCartItem in (:uuidCartItems) AND p.uuidShop = :uuidShop")
    List<CartItem> findShopCartItem(List<String> uuidCartItems, String uuidShop);

    Optional<CartItem> findByUuidCartItemAndUuidCart(String uuidCartItem, String uuidCart);

    @Query(value = "SELECT ci.uuid_Product as uuidProduct, " +
            "ci.uuid_Sku as uuidSku, " +
            "ci.uuid_Cart_Item as uuidCartItem, " +
            "ci.discount as discount, " +
            "ci.active as active, " +
            "ci.quantity as quantity, " +
            "ci.price as unitPrice, " +
            "(SELECT GROUP_CONCAT(pvo.name SEPARATOR ', ')  " +
            "FROM sku_product_variant_option spvo " +
            "JOIN product_variant_option pvo ON spvo.uuid_product_variant_option = pvo.uuid_product_variant_option " +
            "WHERE spvo.uuid_sku = ci.uuid_Sku " +
            "GROUP BY spvo.uuid_sku ) AS productVariantOptions, " +
            "s.name as shopName, " +
            "s.uuid_Shop as uuidShop " +
            "FROM Cart_Item ci " +
            "INNER JOIN Product p ON ci.uuid_Product = p.uuid_Product " +
            "INNER JOIN Shop s ON p.uuid_Shop = s.uuid_Shop " +
            "WHERE ci.uuid_cart_item in (:uuidCartItems) AND ci.uuid_cart = :uuidCart", nativeQuery = true)
    List<CartItemProjection> findAllByUuidCartItemAndUuidCart(@Param("uuidCartItems") List<String> uuidCartItems,
                                                    @Param("uuidCart") String uuidCart);

    CartItem findByUuidCartAndUuidSku(String uuidCart, String uuidSku);

    @Query(value = "SELECT " +
            "p.title as title, " +
            "p.uuid_Product as uuid_Product, " +
            "ci.uuid_Cart_Item as uuid_Cart_Item, " +
            "ci.quantity as quantity, " +
            "(SELECT GROUP_CONCAT(pvo.name SEPARATOR ', ')  " +
            "FROM sku_product_variant_option spvo " +
            "JOIN product_variant_option pvo ON spvo.uuid_product_variant_option = pvo.uuid_product_variant_option " +
            "WHERE spvo.uuid_sku = ci.uuid_Sku " +
            "GROUP BY spvo.uuid_sku) AS productVariantOptions, " +
            "CASE WHEN ci.uuid_Sku IS NULL THEN p.quantity ELSE s.quantity END as stock " +
            "FROM Cart_Item ci " +
            "JOIN Product p ON p.uuid_Product = ci.uuid_Product " +
            "LEFT JOIN Sku s ON s.uuid_Sku = ci.uuid_Sku " +
            "WHERE ci.uuid_Cart_Item = :orderingItems", nativeQuery = true)
    List<OrderItemStockProjection> findOrderingItemStock(List<String> orderingItems);
}
