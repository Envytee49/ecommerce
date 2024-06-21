package org.example.ecommerce.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.brand.exception.BrandNotFoundException;
import org.example.ecommerce.product.dto.request.ProductCreateRequest;
import org.example.ecommerce.product.dto.request.ProductUpdateRequest;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductsResponse;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    ProductsResponse getAllProducts(@RequestParam(required = true) int page,
                                    @RequestParam(required = true) int size) {
        return productService.getAllProducts(page, size);
    }

    @PatchMapping("/{uuidProduct}")
    Product updateProduct(@PathVariable String uuidProduct, @RequestBody ProductUpdateRequest productUpdateRequest) {
        return productService.updateProduct(productUpdateRequest, uuidProduct);
    }

    @DeleteMapping("/{uuidProduct}")
    void deleteProduct(@PathVariable String uuidProduct) {
        productService.deleteProduct(uuidProduct);
    }

    @PostMapping
    ResponseEntity<?> addProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        try {
            Product product = productService.addProduct(productCreateRequest);
            return ResponseEntity.ok(product);
        } catch (BrandNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/{uuidProduct}")
    ProductDetailResponse getProductDetail(@PathVariable String uuidProduct) {
        return productService.getProductByUuid(uuidProduct);
    }


}
