package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.model.ProductAttribute;
import org.example.ecommerce.product.repository.AttributeRepository;
import org.example.ecommerce.product.repository.ProductAttributeRepository;
import org.example.ecommerce.product.service.AttributeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {
    private final AttributeRepository attributeRepository;
    private final ProductAttributeRepository productAttributeRepository;

    @Override
    public Map<String, String> getAttributes(String uuidProduct) {
        List<ProductAttribute> productAttribute = productAttributeRepository
                .findByUuidProduct(uuidProduct);

        Map<String, String> productAttributes =new HashMap<>();
        productAttribute.forEach(attribute ->
                productAttributes.put(attribute.getUuidAttribute(), attribute.getValue()));

        for(Map.Entry<String, String> entry : productAttributes.entrySet()){
            String value  = entry.getValue();
            String key = attributeRepository.findById(entry.getKey()).get().getKey();
            productAttributes.remove(entry.getKey());
            productAttributes.put(key, value);
        }
        return productAttributes;
    }
}
