package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.dto.request.SkuRequest;
import org.example.ecommerce.product.model.Sku;
import org.example.ecommerce.product.repository.SkuRepository;
import org.example.ecommerce.product.service.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {
    private static final Logger log = LoggerFactory.getLogger(SkuServiceImpl.class);
    private final SkuRepository skuRepository;

    @Override
    @Transactional
    public Map<String, String> saveSku(String uuidProduct, Map<String, List<Map<String, SkuRequest>>> skus) {
        log.info("save sku");
        List<SkuRequest> skuRequests = skus.values().stream()
                .flatMap(List::stream)
                .flatMap(map -> map.values().stream())
                .toList();
        List<Sku> skuList = new ArrayList<>();
        Map<String, String> skuMap = new HashMap<>();
        for (SkuRequest skuRequest : skuRequests) {
            Sku sku = Sku
                    .builder()
                    .sku(skuRequest.getSku())
                    .price(skuRequest.getPrice())
                    .quantity(skuRequest.getQuantity())
                    .uuidProduct(uuidProduct)
                    .build();
            skuList.add(sku);
            skuMap.put(skuRequest.getSku(), sku.getUuidSku());
        }
        log.info(skuList.toString());
        log.info(skuMap.entrySet().toString());
        skuRepository.saveAll(skuList);
        return skuMap;
    }
}
