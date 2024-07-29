package org.example.ecommerce.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductResponse;
import org.example.ecommerce.product.service.ProductRetrieverService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/info")
@RequiredArgsConstructor
@Validated
public class ProductInfoController extends AbstractController {
    private final ProductRetrieverService productService;
    // service
    // function interface
    @GetMapping
    ApiResponse<PageDtoOut<ProductResponse>> getAllProducts(
            @Valid @RequestBody PageDtoIn pageDtoIn) {
        return respond(() -> productService.getAll(pageDtoIn));
    }

    @GetMapping("/{uuidProduct}")
    ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable String uuidProduct) {
        return respond(() -> productService.getByUuid(uuidProduct));
    }
}
