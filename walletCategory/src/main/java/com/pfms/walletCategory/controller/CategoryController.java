package com.pfms.walletCategory.controller;

import com.pfms.walletCategory.dto.CategoryNameDTO;
import com.pfms.walletCategory.dto.CategoryNames;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.services.CategoryService;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getall")
    public List<Category> getAll(){
        return categoryService.getAll();
    }

    @PostMapping("/addcategory")
    public ResponseEntity<Map<String, String>> addCategory(@RequestBody Category category){
        Map<String,String> response = new HashMap<>();
        try {
            category.setCategoryName(WordUtils.capitalizeFully(category.getCategoryName()));
            categoryService.addCategory(category);
            response.put("message","Category added successfully");
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            response.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
//
//    @GetMapping("/{id}")
//    public Category getCategory(@PathVariable("id") Long id){
//        return categoryService.getCategory(id);
//    }

//    @PutMapping("/{id}/creditamount")
//    public Category creditTransaction(@PathVariable("id") Long id,@RequestParam("amount") double amount){
//        return  categoryService.creditTransaction(id,amount);
//    }

    @PutMapping("/{userEmail}/{categoryName}/creditamount")
    public Category creditTransaction(@PathVariable("userEmail") String userEmail,@PathVariable("categoryName") String categoryName,@RequestParam("amount") double amount){
        return categoryService.creditTransaction(userEmail, categoryName, amount);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().body("Category Deleted");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userEmail}")
    public List<CategoryNameDTO> getcategoryName(@PathVariable("userEmail")String userEmail){
        return categoryService.getcategoryName(userEmail);
    }

    @GetMapping("/{userEmail}/getName")
    public ResponseEntity<List<String>> getNames(@PathVariable("userEmail")String userEmail){
        return ResponseEntity.ok().body(categoryService.getNames(userEmail));
    }

}
