package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.dto.response.ProductVariantResponse;
import org.example.ecommerce.product.dto.response.VariantOption;
import org.example.ecommerce.product.projections.ProductVariantProjection;
import org.example.ecommerce.product.repository.ProductVariantRepository;
import org.example.ecommerce.product.service.ProductVariantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private static final Logger log = LoggerFactory.getLogger(ProductVariantServiceImpl.class);
    private final ProductVariantRepository productVariantRepository;

    @Override
    public List<ProductVariantResponse> getProductVariants(String uuidProduct) {
        log.info("get product variants");
        List<ProductVariantProjection> productVariantProjections = productVariantRepository.findByUuidProduct(uuidProduct);
        if(productVariantProjections.isEmpty()) return null;

        List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
        for (ProductVariantProjection p : productVariantProjections) {
            ProductVariantResponse productVariant = ProductVariantResponse
                    .builder()
                    .uuidProductVariant(p.getUuidProductVariant())
                    .build();
            if (!productVariantResponses.contains(productVariant)) {
                productVariantResponses.add(productVariant);
                productVariant.setProductVariant(p.getProductVariant());
                productVariant.setVariantOptions(new ArrayList<>());
            }
            productVariantResponses.get(productVariantResponses.indexOf(productVariant)).updateProductVariantOptions(
                    VariantOption.builder()
                            .uuidProductVariantOption(p.getUuidProductVariantOption())
                            .option(p.getVariantOption())
                            .build()
            );
        }
        return productVariantResponses;
    }
}
