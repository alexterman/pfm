package com.noname.digital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noname.digital.Application;
import com.noname.digital.controller.rest.FoundTransaction;
import com.noname.digital.controller.rest.NewTransaction;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Tag;
import com.noname.digital.model.Transaction;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by alex on 3/11/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TagRepository tagRepository;


    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void transactionCrudTest () throws Exception {

        Customer user = createUser();

        NewTransaction newTransaction = new NewTransaction();
        newTransaction.amount = 100.00;
        newTransaction.balanceBefore = 3100.00;
        newTransaction.balanceAfter = 3000.00;
        newTransaction.description = "Test description";
        newTransaction.execution = new Date();


        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(newTransaction);

        mockMvc.perform(post("/customers/" + user.id + "/transactions")
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":1}"));

        ResultActions result = mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        FoundTransaction foundTransaction = mapper.readValue(result.andReturn().getResponse().getContentAsString(), FoundTransaction.class);
        assertThat(foundTransaction.amount).isEqualTo(100.00);
        assertThat(foundTransaction.balanceAfter).isEqualTo(3000.00);
        assertThat(foundTransaction.balanceBefore).isEqualTo(3100.00);
        assertThat(foundTransaction.description).isEqualTo(newTransaction.description);

        Tag tag = new Tag();
        tag.name = "testTag";
        tag.customer = user;
        Tag saved = this.tagRepository.save(tag);

        mockMvc.perform(put("/customers/" + user.id + "/transactions/1/add_tag/" + saved.id)
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        result = mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        foundTransaction = mapper.readValue(result.andReturn().getResponse().getContentAsString(), FoundTransaction.class);
        assertThat(foundTransaction.amount).isEqualTo(100.00);
        assertThat(foundTransaction.balanceAfter).isEqualTo(3000.00);
        assertThat(foundTransaction.balanceBefore).isEqualTo(3100.00);
        assertThat(foundTransaction.description).isEqualTo(newTransaction.description);
        assertThat(foundTransaction.tags.size()).isEqualTo(1);
        assertThat(foundTransaction.tags.iterator().next().name).isEqualTo(tag.name);


        mockMvc.perform(delete("/customers/" + user.id + "/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/customers/" + user.id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"id\":"+user.id+",\"firstName\":\"alex\",\"lastName\":\"terman\",\"categories\":[],\"tags\":[]}"));

        removeUser(user);

    }



    private Customer createUser (){
        Customer customer = new Customer("alex", "terman");
        this.customerRepository.save(customer);
        return customer;
    }

    private void removeUser (Customer customer) {
        this.customerRepository.delete(customer);
    }

}
