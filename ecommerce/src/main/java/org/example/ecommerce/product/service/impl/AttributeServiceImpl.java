package org.example.ecommerce.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.product.model.Attribute;
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
    public Map<String, String> findByUuidProduct(String uuidProduct) {
        List<ProductAttribute> productAttribute = productAttributeRepository
                .findByUuidProduct(uuidProduct);

        Map<String, String> productAttributes = new HashMap<>();
        productAttribute.forEach(attribute ->
                productAttributes.put(attribute.getUuidAttribute(), attribute.getValue()));

        List<Attribute> attributes = attributeRepository.findByUuidAttribute(uuidProduct);

        for (Attribute attribute : attributes) {
            String key = attribute.getUuidAttribute();
            String value = productAttributes.get(key);
            productAttributes.remove(key);
            productAttributes.put(attribute.getKey(), value);
        }
        return productAttributes;
    }
}
