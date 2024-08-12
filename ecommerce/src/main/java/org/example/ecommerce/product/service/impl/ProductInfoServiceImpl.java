package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.product.aop.ProductVariantAOP;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.aop.SearchProductAOP;
import org.example.ecommerce.product.dto.request.ProductVariantDetailRequest;
import org.example.ecommerce.product.dto.request.ReplyProductReviewRequest;
import org.example.ecommerce.product.dto.request.ReviewProductRequest;
import org.example.ecommerce.product.dto.response.*;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.model.ProductReview;
import org.example.ecommerce.product.model.Sku;
import org.example.ecommerce.product.projections.ProductResponseProjection;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.repository.ProductReviewRepository;
import org.example.ecommerce.product.repository.specification.ProductSpecifications;
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
        Page<Product> products = productRepository.findAll(
                ProductSpecifications.withFilters(
                        keyword,
                        sortBy,
                        sortDirection,
                        minPrice,
                        maxPrice,
                        ratingFilter),
                pageRequest);
        List<String> uuidProducts = products.getContent().stream().map(Product::getUuidProduct).toList();
        List<ProductResponseProjection> productProjections = productRepository.findAll(uuidProducts);
        // convert to product response
        List<ProductResponse> productResponse = productProjections
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
    public ProductReviewResponse getProductReviewByUuid(String uuidProduct) {
        return null;
    }

    @Override
    public void reviewProduct(String uuidProduct, ReviewProductRequest request) {
//        String uuidOrder = request.getUuidOrder();
        ProductReview productReview = ProductReview
                .builder()
                .uuidProduct(uuidProduct)
                .uuidUser(SecurityUtils.getCurrentUserUuid())
                .title(request.getTitle())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        productReviewRepository.save(productReview);
    }

    @Override
    public void replyProductReview(String uuidProduct, ReplyProductReviewRequest request) {

    }
}
