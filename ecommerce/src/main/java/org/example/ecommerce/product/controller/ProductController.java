package org.example.ecommerce.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.dto.request.ProductCreateRequest;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductsResponse;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.service.ProductService;
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

    @PutMapping
    Product updateProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        return productService.updateProduct(productCreateRequest);
    }

    @DeleteMapping("/{uuidProduct}")
    void deleteProduct(@PathVariable String uuidProduct) {
        productService.deleteProduct(uuidProduct);
    }

    @PostMapping
    Product addProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        return productService.addProduct(productCreateRequest);
    }

    @GetMapping("/{uuidProduct}")
    ProductDetailResponse getProductDetail(@PathVariable String uuidProduct) {
        return productService.getProductByUuid(uuidProduct);
    }


}
