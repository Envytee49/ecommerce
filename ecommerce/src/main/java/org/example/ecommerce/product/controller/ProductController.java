package org.example.ecommerce.product.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductsResponse;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController extends AbstractController {
    private final ProductService productService;
    // service
    // function interface
    @GetMapping
    ApiResponse<ProductsResponse> getAllProducts(
            @Min(value = 0) @RequestParam int page,
            @Min(value = 20) @RequestParam int size) {
        return this.respond(() -> productService.getAll(page, size));
    }


    @PatchMapping("/{uuidProduct}")
    ApiResponse<?> updateProduct(@PathVariable String uuidProduct,
                              @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        return this.respond(() -> productService.update(uuidProduct, updateProductRequest),
                "Product updated successfully");
    }

    @DeleteMapping("/{uuidProduct}")
    ApiResponse<?> deleteProduct(@PathVariable String uuidProduct) {
        return this.respond(() -> productService.delete(uuidProduct),
                "Product deleted successfully");
    }

    @PostMapping
    ApiResponse<?> addProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        return this.respond(() -> productService.create(createProductRequest),
                HttpStatus.CREATED,
                "Product added successfully");
    }

    @GetMapping("/{uuidProduct}")
    ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable String uuidProduct) {
        return this.respond(() -> productService.getByUuid(uuidProduct));
    }
}
