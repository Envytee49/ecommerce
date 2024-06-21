package org.example.ecommerce;

import org.example.ecommerce.user.model.Brand;
import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.repository.BrandRepository;
import org.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BrandTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Test
    void createBrandTest() {
        User user = userRepository.findByEmail("john.doe@example.com");
        Brand brand = new Brand();
        brand.setName("New House");
        brand.setUuidUser(user.getUuidUser());
        brandRepository.save(brand);
    }

}
