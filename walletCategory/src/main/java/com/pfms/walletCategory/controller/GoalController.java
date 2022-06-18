package com.pfms.walletCategory.controller;

import com.pfms.walletCategory.dto.GoalDTO;
import com.pfms.walletCategory.model.Goal;
import com.pfms.walletCategory.repository.GoalRepository;
import com.pfms.walletCategory.services.CategoryService;
import org.apache.commons.lang.WordUtils;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/goal")
@Transactional
public class GoalController {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/{userEmail}/get")
    public List<Goal> getall(@PathVariable("userEmail")String userEmail){
        return goalRepository.findAllByUserEmail(userEmail);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String,String>> addGoal(@RequestBody GoalDTO goal) throws Exception {
        Map<String,String> response = new HashMap<>();
        try {
            goal.setGoalName(WordUtils.capitalizeFully(goal.getGoalName()));
            goal.setMaxSpent(Double.parseDouble(goal.getMaxSpent()+""));
            categoryService.updateCategory(goal.getCategories(), goal.getUserEmail(),goal);
            response.put("message","Goal added successfully");
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            response.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }

    @DeleteMapping("/remove/{id}")
    public void deleteGoal(@PathVariable("id")Long id){
        goalRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Goal getGoal(@PathVariable("id")Long id){
        return goalRepository.findById(id).get();
    }

}
