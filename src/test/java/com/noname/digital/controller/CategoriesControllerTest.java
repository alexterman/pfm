package com.noname.digital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noname.digital.Application;
import com.noname.digital.controller.rest.NewCategory;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import com.noname.digital.repo.CategoryRepository;
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
 * Created by at13345 on 07/03/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CategoriesControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void categoryNotFoundTest () throws Exception {
        Customer user = createUser();
        mockMvc.perform(get("/customers/" + user.id + "/categories/1"))
                .andExpect(status().isNotFound());
        removeUser();
    }


    @Test
    public void createCategoryTest () throws Exception {
        Customer user = createUser();
        NewCategory newCategory = new NewCategory();
        newCategory.name = "testCategory";

        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(newCategory);

        mockMvc.perform(post("/customers/" + user.id + "/categories")
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":1}"));
        removeUser();
    }

    @Test
    public void deleteCategoryTest () throws Exception {

        Customer user = createUser();
        Category category = this.categoryRepository.save(new Category(user, "deleteCategoryTest"));

        mockMvc.perform(delete("/customers/" + user.id + "/categories/" + category.id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/customers/" + user.id + "/categories/" + category.id))
                .andExpect(status().isNotFound());
        removeUser();

    }

    private Customer createUser (){
        Customer customer = new Customer("alex", "terman");
        this.customerRepository.save(customer);
        return customer;
    }

    private void removeUser () {
        Customer customer = this.customerRepository.findByLastName("terman").get(0);
        this.customerRepository.delete(customer);
    }

}
