package org.example.ecommerce.product.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ProductCategoryId implements Serializable {
    private String uuidProduct;
    private String uuidCategory;
}
