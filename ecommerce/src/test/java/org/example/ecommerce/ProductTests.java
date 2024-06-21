package org.example.ecommerce;

import org.example.ecommerce.product.model.Product;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.brand.model.Brand;
import org.example.ecommerce.brand.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ProductTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Test
    void saveProductTest() {

        Brand brand = brandRepository.findByName("New House");
        Product p = Product.builder()
                .title("Organic Apple")
                .metaTitle("Fresh Organic Apple")
                .summary("Freshly picked organic apples")
                .type(1) // Assuming type 1 corresponds to organic products
                .price(200.0)
                .quantity(50)
                .publishedDate(LocalDateTime.of(2024,12,12,12,12,12))
                .description("These are high-quality organic apples.")
                .uuidBrand(brand.getUuidBrand())
                .build();

        productRepository.save(p);
    }
}
