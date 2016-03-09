package com.noname.digital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noname.digital.Application;
import com.noname.digital.controller.rest.FoundCustomer;
import com.noname.digital.controller.rest.NewCustomer;
import com.noname.digital.repo.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by at13345 on 07/03/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void customerNotFound() throws Exception {
        mockMvc.perform(get("/customers/1")).andExpect(status().isNotFound());
    }

    @Test
    public void customerCreated() throws Exception {
        NewCustomer newCustomer = new NewCustomer();
        newCustomer.firstName = "alex";
        newCustomer.lastName = "terman";

        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(newCustomer);

        mockMvc.perform(post("/customers")
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":1}"))
                ;
        FoundCustomer foundCustomer = new FoundCustomer();
        foundCustomer.firstName = newCustomer.firstName;
        foundCustomer.lastName = newCustomer.lastName;
        foundCustomer.id = 1L;
        foundCustomer.categories = new HashSet<>();
        foundCustomer.tags = new HashSet<>();

        String asString = mapper.writeValueAsString(foundCustomer);
        mockMvc.perform(get("/customers/1"))
                .andExpect(content().string(asString));

        this.customerRepository.delete(1L);

    }

}
