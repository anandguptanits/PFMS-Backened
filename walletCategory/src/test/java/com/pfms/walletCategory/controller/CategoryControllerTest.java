package com.pfms.walletCategory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfms.walletCategory.dto.CategoryNameDTO;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.repository.CategoryRepository;
import com.pfms.walletCategory.services.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.discovery.SelectorResolver;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getAll() throws Exception{

        Category c1=new Category("food","anand@gmail.com");
        Category c2=new Category("wedding","anand@gmail.com");
        Category c3=new Category("travel","anand@gmail.com");

        List<Category> categoryList=Arrays.asList(c1, c2, c3);

        Mockito.when(categoryService.getAll()).thenReturn(categoryList);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/getall"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryName",Matchers.is("food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].categoryName",Matchers.is("wedding")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].categoryName",Matchers.is("travel")));
    }

    @Test
    void addCategory() throws Exception {

        Category c1=new Category("food","anand@gmail.com");

        Mockito.when(categoryService.addCategory(c1)).thenReturn(c1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/category/addcategory")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(c1));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Category added successfully")));
    }

    @Test
    void creditTransaction() throws Exception{
        Category c1=new Category("food","anand@gmail.com");
        c1.setAmountSpent(233D);


        Mockito.when(categoryService.creditTransaction("anand@gmail.com","food",20D))
                .thenReturn(c1.setAmountSpent(253D));

        mockMvc.perform(MockMvcRequestBuilders.put("/category/anand@gmail.com/food/creditamount?amount=20"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountSpent",Matchers.is(253D)));

    }

    @Test
    void deleteCategory_NotFound() throws Exception{
        Mockito.when(categoryRepository.findById(1L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/category/1L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /*
    Comment - while calling deleteById check first if this id exist so that exception can be identify and showm
      @Test
     void deleteCategory_Found() throws Exception{
        Category c1=new Category("food","anand@gmail.com");
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(c1));

        mockMvc.perform(MockMvcRequestBuilders
                      .delete("/category/1L")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }
    */

    @Test
    void getcategoryName() throws Exception {

        CategoryNameDTO cDTO1=new CategoryNameDTO(1L,"food",221D);
        CategoryNameDTO cDTO2=new CategoryNameDTO(1L,"travel",1231D);


        List<CategoryNameDTO> list=Arrays.asList(cDTO1,cDTO2);
        Mockito.when(categoryService.getcategoryName("abc@gmail.com")).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/abc@gmail.com"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryName",Matchers.is("food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].categoryName",Matchers.is("travel")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amountSpent",Matchers.is(221D)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amountSpent",Matchers.is(1231D)));

    }

    @Test
    void getNames() throws Exception {

        List<String> categoryNameList=Arrays.asList("food","wedding","travel");

        Mockito.when(categoryService.getNames("abc@gmail.com")).thenReturn(categoryNameList);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/abc@gmail.com/getName"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",Matchers.is("food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]",Matchers.is("wedding")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]",Matchers.is("travel")));
    }
}