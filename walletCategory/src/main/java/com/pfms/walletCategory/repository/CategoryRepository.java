package com.pfms.walletCategory.repository;

import com.pfms.walletCategory.dto.CategoryNameDTO;
import com.pfms.walletCategory.dto.CategoryNames;
import com.pfms.walletCategory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryNameAndUserEmail(String categoryName, String userEmail);

    Category findByUserEmailAndCategoryName(String userEmail,String categoryName);

    List<Category> findAllByUserEmail(String userEmail);

    @Query(value = "SELECT category_name as categoryName from categories where user_email = ?1",nativeQuery = true)
    List<String> listAllCategories(@Param("user_email") String userEmail);
}
