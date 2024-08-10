package org.example.ecommerce.product.service;

import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.product.dto.request.ReplyProductReviewRequest;
import org.example.ecommerce.product.dto.request.ReviewProductRequest;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductResponse;
import org.example.ecommerce.product.dto.response.ProductReviewResponse;

public interface ProductInfoService {
    PageDtoOut<ProductResponse> getAll(PageDtoIn pageDtoIn,
                                       String keyword,
                                       String sortBy,
                                       String sortDirection,
                                       String minPrice,
                                       String maxPrice,
                                       String ratingFilter);
    ProductDetailResponse getProductByUuid(String uuidProduct);

    ProductReviewResponse getProductReviewByUuid(String uuidProduct);

    void reviewProduct(String uuidProduct, ReviewProductRequest request);

    void replyProductReview(String uuidProduct, ReplyProductReviewRequest request);
}
