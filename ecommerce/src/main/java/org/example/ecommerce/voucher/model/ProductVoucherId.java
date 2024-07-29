package org.example.ecommerce.voucher.model;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class ProductVoucherId implements Serializable {
    private String uuidVoucher;
    private String uuidProduct;
}
