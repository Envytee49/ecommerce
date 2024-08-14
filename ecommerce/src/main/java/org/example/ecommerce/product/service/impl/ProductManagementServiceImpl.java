package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.model.CartItem;
import org.example.ecommerce.configuration.security.SecurityUtils;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.aop.CreateProductAOP;
import org.example.ecommerce.product.dto.request.CreateProductRequest;
import org.example.ecommerce.product.dto.request.ProductVariantRequest;
import org.example.ecommerce.product.dto.request.SkuRequest;
import org.example.ecommerce.product.dto.request.UpdateProductRequest;
import org.example.ecommerce.product.helper.VariantMap;
import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.model.Sku;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.product.repository.ProductVariantRepository;
import org.example.ecommerce.product.repository.SkuRepository;
import org.example.ecommerce.product.service.ProductManagementService;
import org.example.ecommerce.product.service.ProductVariantService;
import org.example.ecommerce.product.service.SPVOService;
import org.example.ecommerce.product.service.SkuService;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductManagementServiceImpl implements ProductManagementService {
    private static final Logger log = LoggerFactory.getLogger(ProductManagementServiceImpl.class);
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final ProductVariantService productVariantService;
    private final CreateProductAOP createProductAOP;
    private final SkuService skuService;
    private final SPVOService spvoService;
    private final SkuRepository skuRepository;

    @Override
    @Transactional
    public void create(CreateProductRequest request) {
        log.info("create product");
        String uuidShop = shopRepository
                .findByUuidSeller(SecurityUtils.getCurrentUserUuid())
                .getUuidShop();
        createProductAOP.checkProductQuantity(request.getSkus(), request.getQuantity());

        Product product = productRepository.save(Product.builder()
                .publishedDate(request.getPublishedDate())
                .price(request.getPrice())
                .title(request.getTitle())
                .description(request.getDescription())
                .metaTitle(request.getMetaTitle())
                .quantity(request.getQuantity())
                .summary(request.getSummary())
                .uuidShop(uuidShop)
                .build());
        log.info("uuidProduct: {}", product.getUuidProduct());
        // save product variant and product variant option
        if (request.getSkus() != null && request.getVariants() != null) {
            Map<String, VariantMap> variantMap = productVariantService.saveProductVariant(
                    product.getUuidProduct(),
                    request.getVariants());
            // save sku
            Map<String, String> skuMap = skuService.saveSku(product.getUuidProduct(), request.getSkus());
            // save sku product variant option
            spvoService.saveSPVO(variantMap, skuMap, request.getSkus());
        }
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

    @Override
    public void updateProductStock(List<CartItem> updatedCartItems) {
        // key : uuidProduct or uuidSku - value : quantity in cart
        Map<String, Integer> cartItemQuantity = new HashMap<>();
        for (CartItem c : updatedCartItems) {
            if (c.getUuidSku() != null) {
                cartItemQuantity.put(c.getUuidSku(), c.getQuantity());
                cartItemQuantity.merge(c.getUuidProduct(), c.getQuantity(), Integer::sum);
            } else {
                cartItemQuantity.put(c.getUuidProduct(), c.getQuantity());
            }
        }
        List<CartItem> soleCartItems = updatedCartItems
                .stream()
                .filter(u -> u.getUuidSku() == null)
                .toList();
        List<Product> soleProducts = productRepository.findAllById(soleCartItems
                .stream()
                .map(CartItem::getUuidProduct)
                .toList());
        for (Product p : soleProducts) {
            p.setQuantity(cartItemQuantity.get(p.getUuidProduct()));
        }

        List<CartItem> skuCartItems = updatedCartItems
                .stream()
                .filter(u -> u.getUuidSku() != null)
                .toList();
        List<Product> skuProducts = productRepository
                .findAllById(skuCartItems
                        .stream()
                        .map(CartItem::getUuidProduct)
                        .toList());
        List<Sku> skus = skuRepository.findAllById(skuCartItems.stream().map(CartItem::getUuidProduct).toList());
        for (Sku s : skus) {
            s.setQuantity(cartItemQuantity.get(s.getUuidSku()));
        }
        for (Product skuProduct : skuProducts) {
            skuProduct.setQuantity(cartItemQuantity.get(skuProduct.getUuidProduct()));
        }
        productRepository.saveAll(Stream.concat(skuProducts.stream(), soleProducts.stream()).toList());
        skuRepository.saveAll(skus);
    }


}
