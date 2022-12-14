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
public class TransferControllerTest {
    @Autowired
    MockMvc mockMvc;

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetTransaction() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/transfer"))
                .andExpect(model().attributeExists("options"))
                .andExpect(model().attributeExists("transactionList"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(model().attributeExists("information"));
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testGetValidateTransfer() throws Exception {
        mockMvc.perform(get("/validatetransfer")
                        .param("accountReceiveId","6")
                        .param("amount","50")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("validatetransfer"))
                .andExpect(model().attributeExists("userreceive"))
                .andExpect(model().attributeExists("transactionIn"));
    }

    @WithMockUser("a.benson@gmail.com")
    @Test
    void testPostValidateTransaction() throws Exception {
        mockMvc.perform(post("/validatetransfer")
                        .param("transactionId", "1")
                        .param("accountSendId", "2")
                        .param("accountReceiveId", "4")
                        .param("amount", "50")
                        .param("comment", "cafe")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}
