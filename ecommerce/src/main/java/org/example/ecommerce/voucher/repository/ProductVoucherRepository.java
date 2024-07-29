package org.example.ecommerce.voucher.repository;

import org.example.ecommerce.voucher.model.ProductVoucher;
import org.example.ecommerce.voucher.model.ProductVoucherId;
import org.example.ecommerce.voucher.projection.IProductVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVoucherRepository extends JpaRepository<ProductVoucher, ProductVoucherId> {
    List<IProductVoucher> findByUuidVoucher(String uuidVoucher);
}
