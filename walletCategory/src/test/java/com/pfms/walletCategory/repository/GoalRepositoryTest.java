package com.pfms.walletCategory.repository;

import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.model.Goal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GoalRepositoryTest {

    @Autowired
    private GoalRepository underTestGoal;

    @Autowired
    private  CategoryRepository  underTestCategory;

    @Test
    void whetherAbleToFindAllGoalsByUserEmail() {
        Goal goal1=new Goal("Goal 1","abc@gmail.com",255D);
        Goal goal2=new Goal("Goal 2","abc@gmail.com",255D);
        Goal goal3=new Goal("Goal 3","abc@gmail.com",255D);

        List<Goal> listGoal= Arrays.asList(goal1,goal2,goal3);
        underTestGoal.saveAll(listGoal);

        List<Goal> expected=underTestGoal.findAllByUserEmail("abc@gmail.com");

        assertEquals(expected,listGoal);

    }

    @Test
    void whetherAbleToFindByFindByUserEmailAndCategories() {

        Category category=new Category("food","abc@gmail.com");
        Goal goal=new Goal("Goal 1","abc@gmail.com",255D);

        underTestGoal.save(goal);
        underTestCategory.save(category);

        Goal expected=underTestGoal.findByUserEmailAndCategories("abc@gmail.com",category);

        assertEquals(expected,null);

    }

}