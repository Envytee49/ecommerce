package org.example.ecommerce.product.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.ResponseData;
import org.example.ecommerce.product.api.*;
import org.example.ecommerce.product.dto.request.*;
import org.example.ecommerce.product.dto.response.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
//@Validated
public class ProductController {
    private final CreateProductApi createProductApi;
    private final UpdateProductApi updateProductApi;
    private final DeleteProductApi deleteProductApi;
    private final FetchProductsApi fetchProductsApi;
    private final FetchProductDetailApi fetchProductDetailApi;

    @GetMapping
    ResponseData<ProductsResponse> getAllProducts(
            @Min(0) @RequestParam int page,
            @Min(20) @RequestParam int size) {
        FetchProductsRequest fetchProductsRequest = new FetchProductsRequest(page, size);
        return fetchProductsApi.execute(fetchProductsRequest);
    }


    @PatchMapping("/{uuidProduct}")
    ApiResponse updateProduct(@PathVariable String uuidProduct,
                              @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        updateProductRequest.setUuidProduct(uuidProduct);
        return updateProductApi.execute(updateProductRequest);
    }

    @DeleteMapping("/{uuidProduct}")
    ApiResponse deleteProduct(@PathVariable String uuidProduct) {
        return deleteProductApi.execute(uuidProduct);
    }

    @PostMapping
    ApiResponse addProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        return createProductApi.execute(createProductRequest);

    }

    @GetMapping("/{uuidProduct}")
    ResponseData<ProductDetailResponse> getProductDetail(@PathVariable String uuidProduct) {
        return fetchProductDetailApi.execute(uuidProduct);
    }
}
