package com.pfms.walletCategory.services;

import com.pfms.walletCategory.dto.CategoryNameDTO;
import com.pfms.walletCategory.dto.CategoryNames;
import com.pfms.walletCategory.dto.GoalDTO;
import com.pfms.walletCategory.exception.WalletCategoryException;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.model.Goal;
import com.pfms.walletCategory.repository.CategoryRepository;
import com.pfms.walletCategory.repository.GoalRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GoalRepository goalRepository;

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category){
        if(categoryRepository.existsByCategoryNameAndUserEmail(category.getCategoryName(), category.getUserEmail())){
            throw new WalletCategoryException("Category with name "+category.getCategoryName()+" is already created");
        }
        return categoryRepository.save(category);
    }

    public void updateCategory(List<String> categories,String userEmail, GoalDTO goal) throws Exception {
        Goal goal1 = new Goal(goal.getGoalName(),goal.getUserEmail(),goal.getMaxSpent());
        for (String category:categories) {
            Category category1 = categoryRepository.findByUserEmailAndCategoryName(userEmail,category);
            if(category1==null){
                throw new Exception(category+" category not found");
            }
            if(goalRepository.findByCategories(category1)!=null){
                throw new Exception("Goal with "+category+" is already present");
            }
            goal1.setCategories(category1);
            goalRepository.save(goal1);
        }
    }

    public Category getCategory(Long id){
        try{
            return categoryRepository.findById(id).get();
        }
        catch (NullPointerException e){
            throw new WalletCategoryException("No category found");
        }
    }

//    public Category creditTransaction(Long id,double amount){
//        try {
//            Category category = categoryRepository.findById(id).get();
//            Goal goal = goalRepository.findByUserEmailAndCategories(userEmail,category);
//            if(goal!=null){
//                goal.setAmountSpent(goal.getAmountSpent()+amount);
//            }
//            return categoryRepository.save(category.setAmountSpent(category.getAmountSpent() + amount));
//        }
//        catch (NullPointerException e){
//            throw new WalletCategoryException("No category found");
//        }
//    }

    public Category creditTransaction(String userEmail,String categoryName,double amount){
        try {
            Category category = categoryRepository.findByUserEmailAndCategoryName(userEmail, categoryName);
            Goal goal = goalRepository.findByUserEmailAndCategories(userEmail,category);
            if(goal!=null){
                goal.setAmountSpent(goal.getAmountSpent()+amount);
            }
            return categoryRepository.save(category.setAmountSpent(category.getAmountSpent() + amount));
        }
        catch (NullPointerException e){
            throw new WalletCategoryException("No category with name "+categoryName+" is found");
        }
    }

    public void deleteCategory(Long id) throws Exception {
        Goal goal=goalRepository.findByCategories(categoryRepository.getOne(id));
        if(goal!=null){
            throw new Exception("Goal with name "+goal.getGoalName()+" is associated with this category");
        }
        categoryRepository.deleteById(id);
    }

    public List<CategoryNameDTO> getcategoryName(String userEmail){
        List<CategoryNameDTO> listDest = new LinkedList<>();
        List<Category> listSource = categoryRepository.findAllByUserEmail(userEmail);
        for (Category category : listSource){
            CategoryNameDTO categoryNameDTO = new CategoryNameDTO();
            BeanUtils.copyProperties(category,categoryNameDTO);
            listDest.add(categoryNameDTO);
        }
        return listDest;
    }

    public List<String> getNames(String userEmail){
            return categoryRepository.listAllCategories(userEmail);
    }


}
