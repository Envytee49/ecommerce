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
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.service.AttributeService;
import org.example.ecommerce.product.service.ProductService;
import org.example.ecommerce.brand.model.Brand;
import org.example.ecommerce.brand.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeService attributeService;

    @Override
    public void addProduct(CreateProductRequest createProductRequest) {

        Brand brand = brandRepository.findById(createProductRequest.getUuidBrand()).orElse(null);
        if (brand == null) {
            throw new AppException(ErrorCode.NO_RESOURCE_FOUND);
        }

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
    public void updateProduct(UpdateProductRequest updateProductRequest) {
        Product product = productRepository.findById(updateProductRequest.getUuidProduct())
                .orElseThrow(() -> new AppException(ErrorCode.NO_RESOURCE_FOUND));
        if (updateProductRequest.getPrice() != null)
            product.setPrice(updateProductRequest.getPrice());

        if (updateProductRequest.getQuantity() != null)
            product.setQuantity(updateProductRequest.getQuantity());

        if (updateProductRequest.getPublishedDate() != null)
            product.setPublishedDate(updateProductRequest.getPublishedDate());

        productRepository.save(product);
    }

    @Override
    public void deleteProduct(String uuidProduct) {
        productRepository.deleteById(uuidProduct);
    }

    @Override
    public ProductsResponse getAllProducts(FetchProductsRequest fetchProductsRequest) {


        int page = fetchProductsRequest.getPage();
        int size = fetchProductsRequest.getSize();
        // get all products
        List<Product> products = productRepository.findAll();

        // get all products by page and size
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Product> productsByPageSize = productRepository.findAll(pageRequest);

        // convert to product response
        List<ProductResponse> productResponse = productsByPageSize
                .stream()
                .map(ProductResponse::from).toList();

        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setTotal(products.size());
        productsResponse.setProducts(productResponse);

        return productsResponse;
    }

    @Override
    public ProductDetailResponse getProductByUuid(String uuidProduct) {
        Product product = productRepository
                .findById(uuidProduct)
                .orElseThrow(() -> new AppException(ErrorCode.NO_RESOURCE_FOUND));

        Brand brand = brandRepository.findById(product.getUuidBrand())
                .orElseThrow(() -> new AppException(ErrorCode.NO_RESOURCE_FOUND));

        List<Category> categories = categoryRepository.findByUuidProduct(uuidProduct);

        Map<String, String> attributes = attributeService.findByUuidProduct(uuidProduct);

        return ProductDetailResponse.from(product, brand.getName(), categories, attributes);
    }
}
