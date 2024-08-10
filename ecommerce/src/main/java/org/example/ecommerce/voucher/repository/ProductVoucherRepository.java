package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.model.ProductVoucher;
import org.example.ecommerce.voucher.model.ProductVoucherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductVoucherRepository extends JpaRepository<ProductVoucher, ProductVoucherId> {

    @Query(value =
            "SELECT ci.uuidProduct FROM CartItem  ci WHERE ci.uuidCartItem in (:uuidCartItems) " +
            "INTERSECT " +
            "SELECT pv.uuidProduct FROM ProductVoucher pv WHERE pv.uuidVoucher = :uuidVoucher")
    List<String> findVoucherProducts(String uuidVoucher, List<String> uuidCartItems);
}
