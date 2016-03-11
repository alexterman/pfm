package com.noname.digital.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noname.digital.Application;
import com.noname.digital.controller.rest.ModifiedTag;
import com.noname.digital.controller.rest.NewTag;
import com.noname.digital.model.Customer;
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
public class TagsControllerTest {

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
    public void tagCrudTest () throws Exception {

        Customer user = createUser();

        NewTag newTag = new NewTag();
        newTag.name = "NewTag";

        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(newTag);

        mockMvc.perform(post("/customers/" + user.id + "/tags")
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":1}"));

        mockMvc.perform(get("/customers/" + user.id + "/tags/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"id\":1,\"name\":\"NewTag\"}"));

        mockMvc.perform(get("/customers/" + user.id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"id\":"+user.id+",\"firstName\":\"alex\",\"lastName\":\"terman\",\"categories\":[],\"tags\":[{\"id\":1,\"name\":\"NewTag\"}]}"));

        ModifiedTag modifiedTag = new ModifiedTag();
        modifiedTag.id = 1;
        modifiedTag.name = "Update";

        value = mapper.writeValueAsString(modifiedTag);

        mockMvc.perform(put("/customers/" + user.id + "/tags")
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string("{\"id\":1}"));


        mockMvc.perform(get("/customers/" + user.id + "/tags/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"id\":1,\"name\":\"Update\"}"));

        mockMvc.perform(get("/customers/" + user.id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"id\":"+user.id+",\"firstName\":\"alex\",\"lastName\":\"terman\",\"categories\":[],\"tags\":[{\"id\":1,\"name\":\"Update\"}]}"));


        mockMvc.perform(delete("/customers/" + user.id + "/tags/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());


        mockMvc.perform(get("/customers/" + user.id + "/tags/1").contentType(MediaType.APPLICATION_JSON))
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
