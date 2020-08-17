package com.shaun.microservice.microservicei;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(SpringRunner.class)
public class MicroServiceIApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    MockMvc mockMvc;

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void A_RESTFUL() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andDo(MockMvcResultHandlers.print());
    }

}
