package org.example.ecommerce.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.ApiResponse;
import org.example.ecommerce.common.controller.AbstractController;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.product.dto.request.ReplyProductReviewRequest;
import org.example.ecommerce.product.dto.request.ReviewProductRequest;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductResponse;
import org.example.ecommerce.product.dto.response.ProductReviewResponse;
import org.example.ecommerce.product.service.ProductInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/info")
@RequiredArgsConstructor
@Validated
public class ProductInfoController extends AbstractController {
    private final ProductInfoService productService;

    // service
    // function interface
    @GetMapping
    ApiResponse<PageDtoOut<ProductResponse>> getAllProducts(
            @Valid @RequestBody PageDtoIn pageDtoIn,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) String ratingFilter) {
        return respond(() -> productService.getAll(pageDtoIn, keyword, sortBy, sortDirection, minPrice, maxPrice, ratingFilter));
    }

    @GetMapping("/{uuidProduct}")
    ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable String uuidProduct) {
        return respond(() -> productService.getProductByUuid(uuidProduct));
    }

    @GetMapping("/reviews/{uuidProduct}")
    ApiResponse<ProductReviewResponse> getProductReview(@PathVariable String uuidProduct) {
        return respond(() -> productService.getProductReviewByUuid(uuidProduct));
    }

    @PostMapping("/reviews/{uuidProduct}")
    @PreAuthorize("hasRole('USER')")
    ApiResponse<?> reviewProduct(@PathVariable String uuidProduct,@Valid @RequestBody ReviewProductRequest request) {
        return respond(() -> productService.reviewProduct(uuidProduct, request), "Product reviewed");
    }

    @PostMapping("/review/reply/{uuidProduct}")
    @PreAuthorize("hasRole('SELLER')")
    ApiResponse<?> replyProductReview(@PathVariable String uuidProduct,@Valid @RequestBody ReplyProductReviewRequest request) {
        return respond(() -> productService.replyProductReview(uuidProduct, request), "Review Replied");
    }


}
