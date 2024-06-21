package org.example.ecommerce;

import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;
@SpringBootTest
public class UserTests {
    @Autowired
    private UserRepository userRepository;
    @Test
    void createUserTest() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setActivate(1);
        user.setDescription("i am john doe");
        user.setMiddleName("Little");
        user.setMobile("0123456789");
        user.setAvatar("link to avatar");
        userRepository.save(user);
    }
    @Test
    void findUserByEmailTest() {
        User user = userRepository.findByEmail("john.doe@example.com");
        assertNotNull(user);
    }
}
