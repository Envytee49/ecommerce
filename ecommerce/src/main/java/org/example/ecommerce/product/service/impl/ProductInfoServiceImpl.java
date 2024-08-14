package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.constants.OrderStatus;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.order.model.Order;
import org.example.ecommerce.order.repository.OrderRepository;
import org.example.ecommerce.product.aop.ProductVariantAOP;
import org.example.ecommerce.product.aop.SearchProductAOP;
import org.example.ecommerce.product.dto.request.ProductVariantDetailRequest;
import org.example.ecommerce.product.dto.request.ReplyProductReviewRequest;
import org.example.ecommerce.product.dto.request.ReviewProductRequest;
import org.example.ecommerce.product.dto.response.*;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.model.ProductReview;
import org.example.ecommerce.product.model.Sku;
import org.example.ecommerce.product.projections.ProductReviewProjection;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.repository.ProductReviewRepository;
import org.example.ecommerce.product.repository.custom.SearchProductRepository;
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
    private final ProductVariantAOP productVariantAOP;
    private final SearchProductAOP searchProductAOP;
    private final ProductReviewRepository productReviewRepository;
    private final SearchProductRepository searchProductRepository;
    private final OrderRepository orderRepository;

    @Override
    public PageDtoOut<ProductResponse> getAll(PageDtoIn pageDtoIn,
                                              String keyword,
                                              String sortBy,
                                              String sortDirection,
                                              Double minPrice,
                                              Double maxPrice,
                                              Integer ratingFilter) {
        searchProductAOP.checkSearchProductParams(minPrice, maxPrice, sortBy, sortDirection);
        // get all products by page and size
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize());
        Page<ProductResponse> productResponses = searchProductRepository.findAllWithFilters(keyword,
                sortBy,
                sortDirection,
                minPrice,
                maxPrice,
                ratingFilter,
                pageRequest);

        return PageDtoOut.from(
                pageDtoIn.getPage(),
                pageDtoIn.getSize(),
                productResponses.getTotalElements(),
                productResponses.getContent());
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
    public ProductVariantDetailResponse getProductVariantDetail(ProductVariantDetailRequest request) {
        Sku sku = productVariantAOP.checkProductVariant(request.getUuidProduct(), request.getProductVariants());
        return ProductVariantDetailResponse.builder()
                .uuidProduct(request.getUuidProduct())
                .productVariants(request.getProductVariants())
                .price(sku.getPrice())
                .stock(sku.getQuantity())
                .build();
    }

    @Override
    public ProductReviewResponse getProductReviewByUuid(String uuidProduct, PageDtoIn pageDtoIn) {
        Pageable pageRequest = PageRequest.of(pageDtoIn.getPage() - 1, pageDtoIn.getSize());
        Page<ProductReviewProjection> projection = productReviewRepository.findProductReview(uuidProduct, pageRequest);
        List<ProductReviewDetailResponse> responses = projection
                .getContent()
                .stream()
                .map(p -> ProductReviewDetailResponse
                        .builder()
                        .uuidProductReview(p.getUuidProductReview())
                        .title(p.getTitle())
                        .rating(p.getRating())
                        .variation(p.getVariation())
                        .comment(p.getComment())
                        .createdAt(p.getCreatedDate())
                        .username(p.getUsername())
                        .build())
                .toList();
        Double averageRating = productReviewRepository.calculateAverageRating(uuidProduct);
        return ProductReviewResponse.from(
                pageDtoIn.getPage(),
                pageDtoIn.getSize(),
                projection.getTotalElements(),
                averageRating,
                responses);
    }

    @Override
    public void reviewProduct(String uuidProduct, ReviewProductRequest request) {
        Product product = productRepository.findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        String uuidOrder = request.getUuidOrder();
        Order order = orderRepository.findByUuidOrderAndUuidUser(uuidOrder,SecurityUtils.getCurrentUserUuid())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if(order.getStatus() != OrderStatus.DELIVERED)
            throw new AppException(ErrorCode.BAD_REQUEST);
        String variation = orderRepository.findProductVariation(uuidProduct, uuidOrder)
                .orElse(null);
        ProductReview productReview = ProductReview
                .builder()
                .uuidProduct(uuidProduct)
                .uuidUser(SecurityUtils.getCurrentUserUuid())
                .title(request.getTitle())
                .rating(request.getRating())
                .comment(request.getComment())
                .variation(variation)
                .build();
        productReviewRepository.save(productReview);
    }

    @Override
    public void replyProductReview(String uuidProduct, ReplyProductReviewRequest request) {

    }
}
