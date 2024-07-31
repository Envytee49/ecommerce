package org.example.ecommerce.cart.repository;

import org.example.ecommerce.cart.dto.response.CartItemResponse;
import org.example.ecommerce.cart.model.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, String> {
    Page<CartItem> findByUuidCart(String uuidCart, Pageable pageable);
    void deleteByUuidCart(String uuidCart);
    CartItem findByUuidCartAndUuidProduct(String uuidCart, String uuidProduct);
}
