package org.example.ecommerce;

import org.example.ecommerce.product.model.Category;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest

public class CategoryTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void insertTest() {
        Category category = Category.builder()
                .content("This is very good")
                .title("holy moly")
                .metaTitle("moly holy")
                .slug("what is this ?")
                .build();
        categoryRepository.save(category);
    }
    @Test
    void getCategoryTest() {
        Category category = categoryRepository.findById("cd8a9591-5824-45ad-8634-f503be7f3f4a")
                .orElse(null);
        assertEquals("holy moly", category.getTitle());
    }
    @Test
    void removeCategoryTest() {
        categoryRepository.deleteAll();
    }
}
