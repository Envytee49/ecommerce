package org.example.ecommerce.voucher.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_voucher")
@IdClass(ProductVoucherId.class)
public class ProductVoucher {
    @Id
    @Size(max = 40)
    @Column(name = "uuid_voucher")
    private String uuidVoucher;

    @Id
    @Size(max = 40)
    @Column(name = "uuid_product")
    private String uuidProduct;
}
