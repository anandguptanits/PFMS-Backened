package com.pfms.walletCategory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfms.walletCategory.model.Category;
import com.pfms.walletCategory.model.Wallet;
import com.pfms.walletCategory.repository.CategoryRepository;
import com.pfms.walletCategory.services.CategoryService;
import com.pfms.walletCategory.services.WalletService;
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

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = WalletController.class)
class WalletControllerTest {

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void addWallet() throws Exception{
        Wallet w1=new Wallet("anand@gmail.com","SBI",230D);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/wallet/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(w1));

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().is(202))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Wallet Added")));
    }

    @Test
    void creditAmount() throws Exception{

        Wallet w1=new Wallet("anand@gmail.com","SBI",230D);

        mockMvc.perform(MockMvcRequestBuilders.put("/wallet/anand@gmail.com/SBI/creditamount?amount=20"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is("Amount is credited to your wallet")));
    }

    @Test
    void debitAmount() throws  Exception{
        Wallet w1=new Wallet("anand@gmail.com","SBI",230D);

        mockMvc.perform(MockMvcRequestBuilders.put("/wallet/anand@gmail.com/SBI/debitamount?amount=20"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is("Amount is debited from your wallet")));
    }

    @Test
    void getBalanceOfAllWallets() throws  Exception{
        Wallet w1=new Wallet("anand@gmail.com","SBI",5D);
        Wallet w2=new Wallet("anand@gmail.com","SBI", 10D);
        Wallet w3=new Wallet("anand@gmail.com","SBI",15D);

        Mockito.when(walletService.getBalanceOfAllWallets("anand@gmail.com"))
                .thenReturn(30D);

        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/anand@gmail.com/balance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.is(30D)));
    }

}