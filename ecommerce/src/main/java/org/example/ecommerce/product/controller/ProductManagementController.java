package org.example.ecommerce.product.controller;

import com.nimbusds.jose.JWSHeader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;
import org.example.ecommerce.product.service.ProductManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/management")
@RequiredArgsConstructor
@Validated
public class ProductManagementController extends AbstractController {
    private final ProductManagementService productManagementService;
    @PatchMapping("/{uuidProduct}")
    ApiResponse<?> updateProduct(@PathVariable String uuidProduct,
                                 @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        return respond(() -> productManagementService.update(uuidProduct, updateProductRequest),
                "Product updated successfully");
    }

    @DeleteMapping("/{uuidProduct}")
    ApiResponse<?> deleteProduct(@PathVariable String uuidProduct) {
        return respond(() -> productManagementService.delete(uuidProduct),
                "Product deleted successfully");
    }

    @PostMapping
    ApiResponse<?> addProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        return respond(() -> productManagementService.create(createProductRequest),
                HttpStatus.CREATED,
                "Product added successfully");
    }

}
