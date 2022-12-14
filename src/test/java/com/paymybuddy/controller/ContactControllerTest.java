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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetContact() throws Exception {
        mockMvc.perform(get("/contact"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("userbuddy"))
                .andExpect(model().attributeExists("information"));
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testPostContact() throws Exception {
        mockMvc.perform(post("/addcontact")
                        .param("firstName", "Alex")
                        .param("lastName", "Benson")
                        .param("email", "a.benson@gmail.com")
                        .param("password", "$2y$10$faEZ0q.A9PyAXFvopXu3kukd9k552dNAgEbaRPkd998xh49U.9u3S")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetAddContact() throws Exception {
        mockMvc.perform(get("/addcontact")
                        .param("email","a.benson@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/addcontact"))
                .andExpect(model().attributeExists("email"));
    }

  /*  @WithMockUser("a.benson@gmail.com")
    @Test
    void testPostAddContact() throws Exception {
        mockMvc.perform(post("/contact")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }*/
}
