package org.example.ecommerce.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.dto.request.AddToCartRequest;
import org.example.ecommerce.cart.dto.request.UpdateCartRequest;
import org.example.ecommerce.cart.dto.response.CartItemResponse;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.cart.service.CartService;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.service.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    @Override
    public PageDtoOut<CartItemResponse> getCart(PageDtoIn pageDtoIn) {
        Pageable page = PageRequest.of(pageDtoIn.getPage()-1, pageDtoIn.getSize());
        Page<CartItem> cartItems = cartRepository.findByUuidCart(SecurityUtils.getCurrentUserCartUuid(), page);

        if(cartItems.isEmpty()) throw new AppException(ErrorCode.CART_NOT_EXIST);

        List<CartItemResponse> cartItemResponses = cartItems
                .stream()
                .map(CartItemResponse::from)
                .toList();

        return PageDtoOut
                .from(
                        pageDtoIn.getPage(),
                        pageDtoIn.getSize(),
                        cartItems.getTotalElements(),
                        cartItemResponses
                        );
    }

    @Override
    public void add(AddToCartRequest addToCartRequest) {
        Product product = productRepository.findById(addToCartRequest
                .getUuidProduct()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        String uuidProduct = addToCartRequest.getUuidProduct();
        String uuidCart = SecurityUtils.getCurrentUserCartUuid();
        CartItem cartItem = cartRepository.findByUuidCartAndUuidProduct(uuidCart, uuidProduct);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.getQuantity());
            cartRepository.save(cartItem);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .uuidCart(uuidCart)
                    .uuidShop(product.getUuidShop())
                    .uuidProduct(uuidProduct)
                    .unitPrice(product.getPrice())
                    .quantity(addToCartRequest.getQuantity())
                    .build();
            cartRepository.save(newCartItem);
        }
    }

    @Override
    public void delete(String uuidCartItem) {
        CartItem cartItem = cartRepository.findById(uuidCartItem)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartRepository.delete(cartItem);
    }

    @Transactional
    @Override
    public void deleteAll() {
        cartRepository.deleteByUuidCart(SecurityUtils.getCurrentUserCartUuid());
    }

    @Override
    public void update(UpdateCartRequest updateCartRequest) {
        CartItem cartItem = cartRepository.findById(updateCartRequest.getUuidCartItem())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.setQuantity(updateCartRequest.getQuantity());
        cartRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getSelectedCartItems(List<String> uuidCartItems) {
        return cartRepository.findByUuidCartItemIn(uuidCartItems);
    }
}
