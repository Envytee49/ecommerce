package org.example.ecommerce.product.service;

import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;

import java.util.List;

public interface ProductManagementService {
    void create(CreateProductRequest createProductRequest);
    void update(String uuidProduct, UpdateProductRequest productCreateRequest);
    void delete(String uuidProduct);

    void updateProductStock(List<CartItem> updatedCartItems);
}
