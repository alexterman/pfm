package com.noname.digital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noname.digital.Application;
import com.noname.digital.controller.rest.NewCategory;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import com.noname.digital.repo.CategoryRepository;
import com.noname.digital.repo.CustomerRepository;
import org.junit.After;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

        Customer customer = new Customer("alex", "terman");
        this.customerRepository.save(customer);

    }

    @After
    public void tearDown () throws Exception {

        this.customerRepository.delete(1L);
    }

    @Test
    public void categoryNotFoundTest () throws Exception {
        mockMvc.perform(get("/customers/1/categories/1"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void createCategoryTest () throws Exception {
        NewCategory newCategory = new NewCategory();
        newCategory.name = "testCategory";
        System.out.println(newCategory);

        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(newCategory);

        mockMvc.perform(post("/customers/1/categories")
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":1}"));

    }

    @Test
    public void deleteCategoryTest () throws Exception {

        this.categoryRepository.save(new Category(this.customerRepository.findOne(1L),"deleteCategoryTest"));

        mockMvc.perform(delete("/customers/1/categories/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/customers/1/categories/1"))
                .andExpect(status().isNotFound());

    }

}
