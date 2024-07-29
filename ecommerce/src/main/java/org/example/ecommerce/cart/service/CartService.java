package org.example.ecommerce.cart.service;

import org.example.ecommerce.cart.dto.request.AddToCartRequest;
import org.example.ecommerce.cart.dto.request.UpdateCartRequest;
import org.example.ecommerce.cart.dto.response.CartItemResponse;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;

import java.util.List;

public interface CartService {
    PageDtoOut<CartItemResponse> getCart(PageDtoIn pageDtoIn);
    void add(AddToCartRequest modifyCartRequest);
    void delete(String uuidCartItem);
    void deleteAll();
    void update(UpdateCartRequest request);
    List<CartItem> getSelectedCartItems(List<String> uuidCartItems);
}
