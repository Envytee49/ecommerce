package org.example.ecommerce.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.dto.request.AddToCartRequest;
import org.example.ecommerce.cart.dto.request.UpdateCartRequest;
import org.example.ecommerce.cart.dto.response.CartResponse;
import org.example.ecommerce.cart.service.CartService;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class CartController extends AbstractController {
    private final CartService cartService;
    @GetMapping()
    public ApiResponse<PageDtoOut<?>> getCart(
                                           @Valid @RequestBody PageDtoIn pageDtoIn) {
        return respond(() -> cartService.getCart(pageDtoIn));
    }

    @PostMapping
    public ApiResponse<?> addToCart(@Valid @RequestBody AddToCartRequest request) {
        return respond(() -> cartService.add(request), "Added to cart successfully");
    }

    @DeleteMapping("/{uuidCartItem}")
    public ApiResponse<?> deleteItem(@PathVariable String uuidCartItem) {
        return respond(() -> cartService.delete(uuidCartItem), "Deleted cart item successfully");
    }

    @DeleteMapping("/all")
    public ApiResponse<?> deleteAll() {
        return respond(() -> cartService.deleteAll(), "Deleted all cart items successfully");
    }

    @PutMapping()
    public ApiResponse<?> updateQuantity( @Valid @RequestBody UpdateCartRequest request) {
        return respond(() -> cartService.update(request), "Updated quantity successfully");
    }
}
