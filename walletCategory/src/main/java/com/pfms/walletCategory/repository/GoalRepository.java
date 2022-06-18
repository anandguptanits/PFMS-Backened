package com.pfms.walletCategory.repository;

import com.pfms.walletCategory.dto.CategoryNames;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal,Long> {

    List<Goal> findAllByUserEmail(String userEmail);

    Goal findByUserEmailAndCategories(String userEmail, Category category);

    Goal findByCategories(Category category);

}
