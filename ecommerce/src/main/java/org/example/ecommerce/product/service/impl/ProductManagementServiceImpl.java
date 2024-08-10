package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.service.ProductManagementService;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductManagementServiceImpl implements ProductManagementService {
    private final ProductRepository productRepository;
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
                .uuidShop(uuidShop)
                .build();
        productRepository.save(savedProduct);
    }

    @Override
    @Transactional
    public void update(String uuidProduct, UpdateProductRequest updateProductRequest) {
        Product product = productRepository.findByUuidProductAndUuidShop(uuidProduct, SecurityUtils.getCurrentSellerShopUuid())
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
        Product product = productRepository.findByUuidProductAndUuidShop(uuidProduct, SecurityUtils.getCurrentSellerShopUuid())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }


}
