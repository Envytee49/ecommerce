package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.dto.request.ReplyProductReviewRequest;
import org.example.ecommerce.product.dto.request.ReviewProductRequest;
import org.example.ecommerce.product.dto.response.*;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.service.ProductInfoService;
import org.example.ecommerce.product.service.ProductVariantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductInfoServiceImpl implements ProductInfoService {
    private static final Logger log = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantService productVariantService;

    @Override
    public PageDtoOut<ProductResponse> getAll(PageDtoIn pageDtoIn,
                                              String keyword,
                                              String sortBy,
                                              String sortDirection,
                                              String minPrice,
                                              String maxPrice,
                                              String ratingFilter) {

        // get all products by page and size
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize());
        Page<Product> products = productRepository.findAll(pageRequest);

        // convert to product response
        List<ProductResponse> productResponse = products
                .stream()
                .map(ProductResponse::from)
                .toList();

        return PageDtoOut.from(
                pageDtoIn.getPage(),
                pageDtoIn.getSize(),
                products.getTotalElements(),
                productResponse);
    }

    @Override
    public ProductDetailResponse getProductByUuid(String uuidProduct) {
        log.info("Get product by uuid: {}", uuidProduct);
        Product product = productRepository
                .findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<ProductVariantResponse> productVariantResponses = productVariantService.getProductVariants(uuidProduct);
        List<CategoryResponse> categories = categoryRepository.findByUuidProduct(uuidProduct);
        return ProductDetailResponse.from(product, productVariantResponses, categories);
    }

    @Override
    public ProductReviewResponse getProductReviewByUuid(String uuidProduct) {
        return null;
    }

    @Override
    public void reviewProduct(String uuidProduct, ReviewProductRequest request) {
        String uuidOrder = request.getUuidOrder();

    }

    @Override
    public void replyProductReview(String uuidProduct, ReplyProductReviewRequest request) {

    }
}
