package org.example.ecommerce;

import org.example.ecommerce.product.model.ProductAttribute;
import org.example.ecommerce.product.repository.AttributeRepository;
import org.example.ecommerce.product.repository.ProductAttributeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AttributeTests {
    @Autowired
    private  AttributeRepository attributeRepository;
    @Autowired
    private  ProductAttributeRepository productAttributeRepository;
    @Test
    void getAttribute() {
        List<ProductAttribute> productAttribute = productAttributeRepository
                .findByUuidProduct("523e4567-e89b-12d3-a456-4266141740001234");
        Map<String, String> productAttributes =new HashMap<>();
        productAttribute.forEach(attribute ->
                productAttributes.put(attribute.getUuidAttribute(), attribute.getValue()));
        for(Map.Entry<String, String> entry : productAttributes.entrySet()){
            String value  = entry.getValue();
            String key = attributeRepository.findById(entry.getKey()).get().getKey();
            productAttributes.remove(entry.getKey());
            productAttributes.put(key, value);
        }

        System.out.println(productAttributes);
    }
}