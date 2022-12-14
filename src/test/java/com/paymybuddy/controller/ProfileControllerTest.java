package com.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {
    @Autowired
    MockMvc mockMvc;

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().attributeExists("balance"));
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetUpdateProfile() throws Exception {
        mockMvc.perform(get("/updateprofile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/updateprofile"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testPostUpdateProfile() throws Exception {
        mockMvc.perform(post("/updateprofile")
                        .param("firstName", "Alex")
                        .param("lastName", "Benson")
                        .param("email", "a.benson@gmail.com")
                        .param("password", "$2y$10$faEZ0q.A9PyAXFvopXu3kukd9k552dNAgEbaRPkd998xh49U.9u3S")
                        .param("nameBank", "orange")
                        .param("iban", "iban")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetTransferProfile() throws Exception {
        mockMvc.perform(get("/transferprofile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/transferprofile"))
                .andExpect(model().attributeExists("balance"))
                .andExpect(model().attributeExists("bankaccount"))
                .andExpect(model().attributeExists("information"));
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testPostTransferProfile() throws Exception {
        mockMvc.perform(post("/validationbank")
                        .param("bankAccountId", "1")
                        .param("userId", "1")
                        .param("name", "orange")
                        .param("iban", "iban")
                        .param("inside", "false")
                        .param("option", "1")
                        .param("amount", "50")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetValidationBank() throws Exception {
        mockMvc.perform(get("/validationbank")
                        .param("option", "1")
                        .param("amount", "50")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("validationbank"))
                .andExpect(model().attributeExists("operation"))
                .andExpect(model().attributeExists("amount"))
                .andExpect(model().attributeExists("option"))
                .andExpect(model().attributeExists("nameaccount"))
                .andExpect(model().attributeExists("iban"));
        ;
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testPostTransferOutside() throws Exception {
        mockMvc.perform(post("/transfertemp")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

}
