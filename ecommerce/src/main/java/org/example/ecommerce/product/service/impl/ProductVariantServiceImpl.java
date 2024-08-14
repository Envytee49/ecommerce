package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.dto.request.ProductVariantRequest;
import org.example.ecommerce.product.dto.response.ProductVariantResponse;
import org.example.ecommerce.product.dto.response.VariantOption;
import org.example.ecommerce.product.helper.VariantMap;
import org.example.ecommerce.product.model.ProductVariant;
import org.example.ecommerce.product.model.ProductVariantOption;
import org.example.ecommerce.product.projections.ProductVariantProjection;
import org.example.ecommerce.product.repository.ProductVariantOptionRepository;
import org.example.ecommerce.product.repository.ProductVariantRepository;
import org.example.ecommerce.product.service.ProductVariantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductVariantServiceImpl implements ProductVariantService {
    private static final Logger log = LoggerFactory.getLogger(ProductVariantServiceImpl.class);
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantOptionRepository productVariantOptionRepository;

    @Override
    public List<ProductVariantResponse> getProductVariants(String uuidProduct) {
        log.info("get product variants");
        List<ProductVariantProjection> productVariantProjections = productVariantRepository.findByUuidProduct(uuidProduct);
        if (productVariantProjections.isEmpty()) return null;

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

    @Override
    @Transactional
    public Map<String, VariantMap> saveProductVariant(String uuidProduct, List<ProductVariantRequest> variants) {
        log.info("save product variant");
        Map<String, VariantMap> variantMap = new HashMap<>();
        List<ProductVariant> productVariants = new ArrayList<>();
        List<ProductVariantOption> productVariantOptions = new ArrayList<>();
        for (ProductVariantRequest variant : variants) {
            ProductVariant productVariant = ProductVariant.builder()
                    .uuidProduct(uuidProduct)
                    .name(variant.getVariantName())
                    .build();
            productVariants.add(productVariant);
            List<String> options = variant.getProductVariantOptions();
            for (String option : options) {
                ProductVariantOption productVariantOption = ProductVariantOption
                        .builder()
                        .uuidProductVariant(productVariant.getUuidProductVariant())
                        .name(option)
                        .build();
                productVariantOptions.add(productVariantOption);
                variantMap.put(option, VariantMap
                        .builder()
                        .uuidProductVariant(productVariant.getUuidProductVariant())
                        .uuidProductVariantOption(productVariantOption.getUuidProductVariantOption())
                        .build());
            }
        }
        productVariantRepository.saveAll(productVariants);
        productVariantOptionRepository.saveAll(productVariantOptions);
        return variantMap;
    }
}
