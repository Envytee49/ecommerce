package org.example.ecommerce.cart.repository;

import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.projection.CartItemProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, String> {
    Page<CartItem> findByUuidCart(String uuidCart, Pageable pageable);
    void deleteByUuidCart(String uuidCart);
    CartItem findByUuidCartAndUuidProduct(String uuidCart, String uuidProduct);

//    @Query("SELECT ci as cartItem, p.uuidShop as uuidShop " +
//            "FROM CartItem ci " +
//            "INNER  JOIN Product p ON p.uuidProduct = ci.uuidProduct " +
//            "WHERE ci.uuidCartItem in (:uuidCartItems)")
//    List<CartItemProjection> findCartItemAndShop(List<String> uuidCartItems);
}
