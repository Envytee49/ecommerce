package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.PageDtoIn;
import org.example.ecommerce.common.dto.PageDtoOut;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;
import org.example.ecommerce.product.dto.response.CategoryResponse;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductResponse;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.projections.IAttribute;
import org.example.ecommerce.product.repository.AttributeRepository;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.service.ProductManagementService;
import org.example.ecommerce.product.service.ProductRetrieverService;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductRetrieverService, ProductManagementService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final ShopRepository shopRepository;

    @Override
    @Transactional
    public void create(CreateProductRequest createProductRequest) {
        String uuidShop = shopRepository
                .findByUuidSeller(SecurityUtils.getCurrentUserUuid())
                .getUuidShop();
        Product savedProduct = Product.builder()
                .publishedDate(createProductRequest.getPublishedDate())
                .price(createProductRequest.getPrice())
                .title(createProductRequest.getTitle())
                .description(createProductRequest.getDescription())
                .metaTitle(createProductRequest.getMetaTitle())
                .quantity(createProductRequest.getQuantity())
                .summary(createProductRequest.getSummary())
                .type(createProductRequest.getType())
                .uuidShop(uuidShop)
                .build();
        productRepository.save(savedProduct);
    }

    @Override
    @Transactional
    public void update(String uuidProduct, UpdateProductRequest updateProductRequest) {
        Product product = productRepository.findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (updateProductRequest.getPrice() != null)
            product.setPrice(updateProductRequest.getPrice());

        if (updateProductRequest.getQuantity() != null)
            product.setQuantity(updateProductRequest.getQuantity());

        if (updateProductRequest.getPublishedDate() != null)
            product.setPublishedDate(updateProductRequest.getPublishedDate());

        productRepository.save(product);
        productRepository.flush();
    }

    @Override
    @Transactional
    public void delete(String uuidProduct) {
        Product product = productRepository.findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    @Override
    public PageDtoOut<ProductResponse> getAll(PageDtoIn pageDtoIn) {

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
    public ProductDetailResponse getByUuid(String uuidProduct) {
        Product product = productRepository
                .findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Map<String, String> attributes = getProductAttributes(uuidProduct);
        List<CategoryResponse> categories = categoryRepository.findByUuidProduct(uuidProduct);
        return ProductDetailResponse.from(product, attributes, categories);
    }

    private Map<String, String> getProductAttributes(String uuidProduct) {
        Map<String, String> productAttributes = new HashMap<>();
        List<IAttribute> attributes = attributeRepository.findByUuidProduct(uuidProduct);
        attributes.forEach(attribute -> productAttributes.put(attribute.getKey(), attribute.getValue()));
        return productAttributes;
    }
}
