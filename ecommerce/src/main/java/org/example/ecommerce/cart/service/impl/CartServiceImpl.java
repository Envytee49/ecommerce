package org.example.ecommerce.cart.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.cart.aop.AddToCartValidation;
import org.example.ecommerce.cart.dto.request.AddToCartRequest;
import org.example.ecommerce.cart.dto.request.ProductVariantRequest;
import org.example.ecommerce.cart.dto.request.UpdateCartRequest;
import org.example.ecommerce.cart.dto.response.CartItemResponse;
import org.example.ecommerce.cart.dto.response.CartResponse;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.cart.projection.CartItemProjection;
import org.example.ecommerce.cart.repository.CartRepository;
import org.example.ecommerce.cart.service.CartService;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.model.Sku;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.repository.ProductVariantRepository;
import org.example.ecommerce.product.repository.SkuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AddToCartValidation addToCartValidation;

    @Override
    public PageDtoOut<CartResponse> getCart(PageDtoIn pageDtoIn) {
        Pageable page = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize());
        Page<CartItemProjection> cartItemProjections =
                cartRepository.findCartItemAndShop(SecurityUtils.getCurrentUserCartUuid(), page);

        if (cartItemProjections.isEmpty()) throw new AppException(ErrorCode.CART_NOT_EXIST);

        List<CartItemProjection> cartItemsList = cartItemProjections.getContent();

        List<CartResponse> cartResponses = new ArrayList<>();

        for (CartItemProjection cartItem : cartItemsList) {
            CartResponse cartResponse = CartResponse.builder()
                    .uuidShop(cartItem.getUuidShop())
                    .build();
            if (!cartResponses.contains(cartResponse)) {
                cartResponse.setShopName(cartItem.getShopName());
                cartResponse.setCartItems(new ArrayList<>());
                cartResponses.add(cartResponse);
            }
            cartResponses.get(cartResponses.indexOf(cartResponse)).updateCartItemList(
                    CartItemResponse.from(cartItem));
        }

        return PageDtoOut
                .from(
                        pageDtoIn.getPage(),
                        pageDtoIn.getSize(),
                        cartResponses.size(),
                        cartResponses
                );
    }

    @Override
    public void add(AddToCartRequest addToCartRequest) {
        log.info("Add to cart");
        List<ProductVariantRequest> productVariantRequests = addToCartRequest.getProductVariants();
        Product product = productRepository.findById(addToCartRequest
                .getUuidProduct()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        Sku sku = addToCartValidation.checkProductVariant(product.getUuidProduct(), productVariantRequests);

        String uuidProduct = addToCartRequest.getUuidProduct();
        String uuidCart = SecurityUtils.getCurrentUserCartUuid();
        String uuidSku = sku == null ? null : sku.getUuidSku();
        CartItem cartItem;
        if(uuidSku == null) {
            if(product.getQuantity() - addToCartRequest.getQuantity() < 0)
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
            cartItem = cartRepository.findByUuidCartAndUuidProduct(uuidCart, uuidProduct);
        } else {
            if(sku.getQuantity() - addToCartRequest.getQuantity() < 0)
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
            cartItem = cartRepository.findByUuidCartAndUuidSku(uuidCart, uuidSku);
        }

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.getQuantity());
            cartRepository.save(cartItem);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .uuidCart(uuidCart)
                    .uuidProduct(uuidProduct)
                    .discount(product.getDiscount())
                    .uuidSku(uuidSku)
                    .unitPrice(product.getPrice())
                    .quantity(addToCartRequest.getQuantity())
                    .build();
            cartRepository.save(newCartItem);
        }
    }

    @Override
    public void delete(String uuidCartItem) {
        CartItem cartItem = cartRepository
                .findByUuidCartItemAndUuidCart(
                        uuidCartItem,
                        SecurityUtils.getCurrentUserCartUuid())
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
        CartItem cartItem = cartRepository
                .findByUuidCartItemAndUuidCart(
                        updateCartRequest.getUuidCartItem(),
                        SecurityUtils.getCurrentUserCartUuid())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.setQuantity(updateCartRequest.getQuantity());
        cartRepository.save(cartItem);
    }

}
