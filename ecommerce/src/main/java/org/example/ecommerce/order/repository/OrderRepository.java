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
    @Query(value = "SELECT " +
            "p.title as productTitle, " +
            "p.uuid_Shop as uuidShop, " +
            "s.name as shopName, " +
            "oi.uuid_Product as uuidProduct, " +
            "oi.uuid_Order_Item as uuidOrderItem, " +
            "oi.uuid_sku as uuidSku, " +
            "oi.quantity as quantity, " +
            "oi.price as originalPrice, " +
            "oi.discount as discountPercentage, " +
            "o.shipping_Discount_Subtotal as shippingDiscountSubtotal, " +
            "o.shipping_Subtotal as shippingSubtotal, " +
            "o.total_Payment as totalPayment, " +
            "o.uuid_Order as uuidOrder, " +
            "(SELECT GROUP_CONCAT(pvo.name SEPARATOR ', ') " +
            "FROM sku_product_variant_option spvo " +
            "JOIN product_variant_option pvo ON spvo.uuid_product_variant_option = pvo.uuid_product_variant_option " +
            "WHERE spvo.uuid_sku = oi.uuid_Sku " +
            "GROUP BY spvo.uuid_sku) AS productVariantOption " +
            "FROM Order_Item oi " +
            "INNER JOIN Product p ON oi.uuid_Product = p.uuid_Product " +
            "INNER JOIN `Order` o ON oi.uuid_Order = o.uuid_Order " +
            "INNER JOIN Shop s ON s.uuid_Shop = p.uuid_Shop " +
            "WHERE o.uuid_User = :uuidUser AND o.status = :orderStatus", nativeQuery = true)
    List<OrderItemProjection> findOrderHistory(@Param("uuidUser") String uuidUser,
                                               @Param("orderStatus") String orderStatus);
    // product p  productName, uuidShop
    // shop s shopName
    // order item uuidProduct, quantity, original price, discount price
    // order shippingFee, total, uuidOrder

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

    Optional<Order> findByUuidOrderAndUuidUser(String uuidOrder, String uuidUser);

    @Query(value = "SELECT GROUP_CONCAT(pvo.name SEPARATOR ', ') " +
            "FROM sku_product_variant_option spvo " +
            "JOIN order_item oi ON oi.uuid_sku = spvo.uuid_sku " +
            "JOIN product_variant_option pvo ON spvo.uuid_product_variant_option = pvo.uuid_product_variant_option " +
            "WHERE oi.uuid_order = :uuidOrder AND oi.uuid_product = :uuidProduct " +
            "GROUP BY oi.uuid_product ", nativeQuery = true)
    Optional<String> findProductVariation(String uuidProduct, String uuidOrder);
}
