package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.dto.request.SkuRequest;
import org.example.ecommerce.product.helper.VariantMap;
import org.example.ecommerce.product.model.SkuProductVariantOption;
import org.example.ecommerce.product.repository.SkuProductVariantOptionRepository;
import org.example.ecommerce.product.service.SPVOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SPVOServiceImpl implements SPVOService {
    private static final Logger log = LoggerFactory.getLogger(SPVOServiceImpl.class);
    private final SkuProductVariantOptionRepository skuProductVariantOptionRepository;

    @Override
    @Transactional
    public void saveSPVO(Map<String, VariantMap> variantMap, Map<String, String> skuMap, Map<String, List<Map<String, SkuRequest>>> skus) {
        log.info("save spvo");
        List<SkuProductVariantOption> spvoList = new ArrayList<>();
        for(Map.Entry<String, List<Map<String, SkuRequest>>> sku : skus.entrySet()) {
            // variant
            String option1 = sku.getKey();
            String uuidProductVariant1 = variantMap.get(option1).getUuidProductVariant();
            String uuidProductVariantOption1 = variantMap.get(option1).getUuidProductVariantOption();
            String uuidSku = null;
            for(Map<String, SkuRequest> optionMap : sku.getValue()) {
                for(Map.Entry<String, SkuRequest> skuOption : optionMap.entrySet()) {
                    // option
                    String option2 = skuOption.getKey();
                    String uuidProductVariant2 = variantMap.get(option2).getUuidProductVariant();
                    String uuidProductVariantOption2 = variantMap.get(option2).getUuidProductVariantOption();

                    // sku
                    String skuName = skuOption.getValue().getSku();
                    uuidSku = skuMap.get(skuName);
                    log.info("uuidProductVariant2 {}", uuidProductVariant2);
                    log.info("uuidProductVariantOption2 {}", uuidProductVariantOption2);
                    log.info("uuidSku {}", uuidSku);
                    spvoList.add(SkuProductVariantOption.builder()
                            .uuidProductVariant(uuidProductVariant2)
                            .uuidProductVariantOption(uuidProductVariantOption2)
                            .uuidSku(uuidSku)
                            .build());
                }
            }
            log.info("uuidProductVariant1 {}", uuidProductVariant1);
            log.info("uuidProductVariantOption1 {}", uuidProductVariantOption1);
            log.info("uuidSku {}", uuidSku);
            spvoList.add(SkuProductVariantOption.builder()
                    .uuidProductVariant(uuidProductVariant1)
                    .uuidProductVariantOption(uuidProductVariantOption1)
                    .uuidSku(uuidSku)
                    .build());

        }
        skuProductVariantOptionRepository.saveAll(spvoList);
    }
}
