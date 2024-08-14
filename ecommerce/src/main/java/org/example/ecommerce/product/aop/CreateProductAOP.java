package org.example.ecommerce.product.aop;

import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.product.dto.request.SkuRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CreateProductAOP {
    private static final Logger log = LoggerFactory.getLogger(CreateProductAOP.class);

    public void checkProductQuantity(Map<String, List<Map<String, SkuRequest>>> skus, int totalQuantity) {
        if (skus != null) {
            log.info("Check product quantitty");
            int skuQuantity = skus.values().stream()
                    .flatMap(List::stream) // Flatten the list of maps
                    .flatMap(map -> map.values().stream()) // Flatten the maps to SkuRequest
                    .mapToInt(SkuRequest::getQuantity)
                    .sum();
            if(skuQuantity != totalQuantity)
                throw new AppException(ErrorCode.MISMATCH_QUANTITY);
        }
    }
}
