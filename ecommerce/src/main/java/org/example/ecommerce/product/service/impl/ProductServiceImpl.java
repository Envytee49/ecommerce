package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.dto.request.ProductCreateRequest;
import org.example.ecommerce.product.dto.response.ProductDetailResponse;
import org.example.ecommerce.product.exception.ProductNotFoundException;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product addProduct(ProductCreateRequest productCreateRequest) {
        Product product = Product.builder()
                .publishedDate(productCreateRequest.getPublishedDate())
                .price(productCreateRequest.getPrice())
                .title(productCreateRequest.getTitle())
                .description(productCreateRequest.getDescription())
                .metaTitle(productCreateRequest.getMetaTitle())
                .quantity(productCreateRequest.getQuantity())
                .summary(productCreateRequest.getSummary())
                .type(productCreateRequest.getType())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(ProductCreateRequest productCreateRequest) {
        return null;
    }

    @Override
    public void deleteProduct(String uuidProduct) {
        productRepository.deleteById(uuidProduct);
    }

    @Override
    public List<ProductDetailResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDetailResponse::from).toList();
    }

    @Override
    public ProductDetailResponse getProductByUuid(String uuidProduct) {
        Product product = productRepository
                .findById(uuidProduct)
                .orElseThrow(() -> new ProductNotFoundException(uuidProduct));
        return ProductDetailResponse.from(product);
    }
}
