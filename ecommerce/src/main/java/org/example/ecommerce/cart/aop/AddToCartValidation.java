package org.example.ecommerce.cart.aop;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.cart.dto.request.ProductVariantRequest;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.model.Sku;
import org.example.ecommerce.product.repository.ProductVariantRepository;
import org.example.ecommerce.product.repository.SkuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddToCartValidation {
    private static final Logger log = LoggerFactory.getLogger(AddToCartValidation.class);
    private final ProductVariantRepository productVariantRepository;
    private final SkuRepository skuRepository;

    public Sku checkProductVariant(String uuidProduct, List<ProductVariantRequest> productVariantRequests) {
        int productVariantCount = productVariantRepository.countProductVariant(uuidProduct);
        if (productVariantRequests != null) {
            log.info("Product variant exists");
            if (!productVariantRequests.isEmpty()) {
                if (productVariantCount == 0) {
                    log.error("Product variant is empty");
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
                if (productVariantRequests.size() < productVariantCount) {
                    log.error("Product variant request is too small");
                    throw new AppException(ErrorCode.BAD_REQUEST.setMessage("Please select all options"));
                }
                if (productVariantRequests.size() > productVariantCount) {
                    log.error("Product variant request is too big");
                    throw new AppException(ErrorCode.BAD_REQUEST.setMessage("Please select at most " + productVariantCount + " options"));
                }
            }
            List<String> productVariants = productVariantRequests
                    .stream()
                    .map(ProductVariantRequest::getUuidProductVariant)
                    .toList();
            List<String> productVariantOptions = productVariantRequests
                    .stream()
                    .map(ProductVariantRequest::getUuidProductVariantOption)
                    .toList();

            Sku sku = skuRepository.findByVariantOption(uuidProduct, productVariants, productVariantOptions)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_FOUND));
            return sku;
        }
        if (productVariantCount > 0) {
            log.error("Please select all options");
            throw new AppException(ErrorCode.BAD_REQUEST.setMessage("productVariants must not be null or empty"));
        }
        return null;
    }
}
