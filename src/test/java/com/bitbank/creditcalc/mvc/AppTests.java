package com.bitbank.creditcalc.mvc;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AppTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void mainPage() throws Exception {
        mockMvc.perform(get("/creditcalc"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void mainPageResult() throws Exception {
        mockMvc.perform(get("/creditcalc?orderId=orderr&limit=5000"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("credit_limit", 5000l))
                .andExpect(model().attribute("credit_term", 24l))
                .andExpect(model().attribute("credit_orderId", "orderr"));
    }

    @Test
    public void sendDataBadRequest() throws Exception {
        mockMvc.perform(post("/creditcalc/senddata"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Ignore
    @Test
    public void sendData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/creditcalc/senddata").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
