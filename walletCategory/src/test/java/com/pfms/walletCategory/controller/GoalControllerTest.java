package com.pfms.walletCategory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfms.walletCategory.dto.GoalDTO;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.model.Goal;
import com.pfms.walletCategory.repository.CategoryRepository;
import com.pfms.walletCategory.repository.GoalRepository;
import com.pfms.walletCategory.services.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = GoalController.class)
class GoalControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private GoalRepository goalRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getall() throws Exception {

        Goal g1=new Goal("Goal1","anand@gmail.com",220D);
        Goal g2=new Goal("Goal2","anand@gmail.com",320D);
        Goal g3=new Goal("Goal3","anand@gmail.com",420D);

        List<Goal> goalList= Arrays.asList(g1, g2, g3);

        Mockito.when(goalRepository.findAllByUserEmail("anand@gmail.com")).thenReturn(goalList);

        mockMvc.perform(MockMvcRequestBuilders.get("/goal/anand@gmail.com/get"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].goalName",Matchers.is("Goal1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].goalName",Matchers.is("Goal2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].goalName",Matchers.is("Goal3")));
    }

    @Test
    void addGoal() throws  Exception{

        List<String> categoryNameList=Arrays.asList("food","wedding","travel");
        GoalDTO g1=new GoalDTO("Goal1","anand@gmail.com",3000D,200D,categoryNameList);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/goal/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(g1));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Goal added successfully")));
    }

    /*

    @Test
    void getGoal() throws  Exception{
        Goal g1=new Goal("Goal1","anand@gmail.com",220D);

        Mockito.when(goalRepository.findById(1L)).thenReturn(Optional.of(g1));

        mockMvc.perform(MockMvcRequestBuilders.get("/goal/1L"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goalName",Matchers.is("Goal1")));

    }

     */
}