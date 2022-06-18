package com.pfms.walletCategory.repository;

import com.pfms.walletCategory.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {


    @Autowired
    private CategoryRepository underTest;

    @BeforeEach
    void setUp() {

    }

    @Test
    void itShouldCheckIfExistsByCategoryNameAndUserEmail() {
        Category category=new Category("food","anand@gmail.com");
        underTest.save(category);

        boolean expected=underTest.existsByCategoryNameAndUserEmail("food","anand@gmail.com");

        assertThat(expected).isTrue();

    }

    @Test
    void whetherAbleToFindByUserEmailAndCategoryName() {
        Category category=new Category("food","anand@gmail.com");
        underTest.save(category);

        Category expectedCategory=underTest.findByUserEmailAndCategoryName("anand@gmail.com","food");

        assertEquals(category,expectedCategory);
    }

    @Test
    void whetherAbleToFindUserEmail() {

        Category c1=new Category("food","anand@gmail.com");
        Category c2=new Category("wedding","anand@gmail.com");
        Category c3=new Category("travel","anand@gmail.com");

        List<Category> listCategory= Arrays.asList(c1,c2,c3);

        underTest.saveAll(listCategory);

        List<Category> expectedList=underTest.findAllByUserEmail("anand@gmail.com");

        assertEquals(expectedList,listCategory);
    }

}