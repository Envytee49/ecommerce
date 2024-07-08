package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.dto.request.*;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.dto.response.ProductResponse;
import org.example.ecommerce.product.dto.response.ProductsResponse;
import org.example.ecommerce.product.model.Category;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.AttributeRepository;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.product.repository.IAttribute;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.service.ProductService;
import org.example.ecommerce.brand.model.Brand;
import org.example.ecommerce.brand.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    @Override
    public void create(CreateProductRequest createProductRequest) {

        brandRepository.findById(createProductRequest.getUuidBrand())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        Product savedProduct = Product.builder()
                .publishedDate(createProductRequest.getPublishedDate())
                .price(createProductRequest.getPrice())
                .title(createProductRequest.getTitle())
                .description(createProductRequest.getDescription())
                .metaTitle(createProductRequest.getMetaTitle())
                .quantity(createProductRequest.getQuantity())
                .summary(createProductRequest.getSummary())
                .type(createProductRequest.getType())
                .uuidBrand(createProductRequest.getUuidBrand())
                .build();
        productRepository.save(savedProduct);
    }

    @Override
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
    }

    @Override
    public void delete(String uuidProduct) {
        Product product = productRepository.findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    @Override
    public ProductsResponse getAll(int page, int size) {

        // get all products by page and size
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageRequest);

        // convert to product response
        List<ProductResponse> productResponse = products
                .stream()
                .map(ProductResponse::from)
                .toList();

        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setTotal(products.getTotalElements());
        productsResponse.setProducts(productResponse);

        return productsResponse;
    }

    @Override
    public ProductDetailResponse getByUuid(String uuidProduct) {
        Product product = productRepository
                .findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Brand brand = brandRepository.findById(product.getUuidBrand())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        List<Category> categories = categoryRepository.findByUuidProduct(uuidProduct);

        Map<String, String> attributes = getProductAttributes(uuidProduct);

        return ProductDetailResponse.from(product, brand.getName(), categories, attributes);
    }

    private Map<String, String> getProductAttributes(String uuidProduct) {
        Map<String, String> productAttributes = new HashMap<>();
        List<IAttribute> attributes = attributeRepository.findByUuidProduct(uuidProduct);
        attributes.forEach(attribute -> productAttributes.put(attribute.getKey(), attribute.getValue()));
        return productAttributes;
    }
}
