package com.test.fakeapitest.util;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CartTest {


    @Autowired
    MockMvc mockMvc;


    @Test
    public void addCartTest() throws Exception{

        mockMvc
                .perform(delete("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                )
                .andExpect(status().isOk())
                .andReturn();
    }
}
