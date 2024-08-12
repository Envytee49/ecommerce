package org.example.ecommerce.order.repository;

import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.projection.OrderItemProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    // product p  productName, uuidShop
    // shop s shopName
    // order item uuidProduct, quantity, original price, discount price
    // order shippingFee, total, uuidOrder
    @Query(value = "SELECT p.title as productTitle, p.uuidShop as uuidShop, " +
            "s.name as shopName, " +
            "oi.uuidProduct as uuidProduct, oi.quantity as quantity, oi.price as originalPrice, oi.discount as discountPercentage, " +
            "o.shippingDiscountSubtotal as shippingDiscountSubtotal, " +
            "o.shippingSubtotal as shippingSubtotal, " +
            "o.totalPayment as totalPayment, " +
            "o.uuidOrder as uuidOrder " +
            "FROM OrderItem oi " +
            "INNER JOIN Product p ON oi.uuidProduct = p.uuidProduct " +
            "INNER JOIN Order o ON oi.uuidOrder = o.uuidOrder " +
            "INNER join Shop s ON s.uuidShop = p.uuidShop " +
            "WHERE o.uuidUser = :uuidUser AND o.status = :orderStatus")
    List<OrderItemProjection> findOrderHistory(@Param("uuidUser") String uuidUser,
                                               @Param("orderStatus") OrderStatus orderStatus);

    // product p  productName, uuidShop, uuidProduct
    // shop s shopName, uuidShop
    // order item uuidProduct, quantity, original price, discount price
    // order shippingFee, total, uuidOrder
    @Query(value = "SELECT p.title as productTitle, p.uuidShop as uuidShop, " +
            "s.name as shopName, " +
            "oi.uuidProduct as uuidProduct, oi.quantity as quantity, oi.price as originalPrice, oi.discount as discountPrice, " +
            "o.merchandiseSubtotal as merchandiseSubtotal, " +
            "o.shippingSubtotal as shippingSubtotal, " +
            "o.shippingDiscountSubtotal as shippingDiscountSubtotal, " +
            "o.voucherDiscount as voucherDiscount, " +
            "o.totalPayment as totalPayment, " +
            "o.uuidOrder as uuidOrder, o.uuidUAddress as uuidUAddress " +
            "FROM OrderItem oi " +
            "INNER JOIN Product p ON oi.uuidProduct = p.uuidProduct " +
            "INNER JOIN Order o ON oi.uuidOrder = o.uuidOrder " +
            "INNER join Shop s ON s.uuidShop = p.uuidShop " +
            "WHERE o.uuidOrder = :uuidOrder AND s.uuidShop = :uuidShop AND o.uuidUser = :uuidUser")
    List<OrderItemProjection> findOrderDetail(@Param("uuidOrder") String uuidOrder,
                                        @Param("uuidShop") String uuidShop,
                                              String uuidUser);

    Page<Order> findByUuidShopAndCreatedDateBetween(String uuidShop, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<Order> findByUuidOrderAndUuidShop(String uuidOrder, String currentSellerShopUuid);
}
