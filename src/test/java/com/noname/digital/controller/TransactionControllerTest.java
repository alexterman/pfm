package com.noname.digital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noname.digital.Application;
import com.noname.digital.controller.rest.Created;
import com.noname.digital.controller.rest.FoundTransaction;
import com.noname.digital.controller.rest.NewTransaction;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Tag;
import com.noname.digital.repo.CategoryRepository;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
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
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void transactionCrudTest () throws Exception {

        Customer user = createUser();

        //create transaction
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

        //add existed tag to a transaction
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

        //add new tag to a transaction
        result = mockMvc.perform(put("/customers/" + user.id + "/transactions/1/addnew_tag/" + "ontheflytag")
                .content(value)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Created created = mapper.readValue(result.andReturn().getResponse().getContentAsString(), Created.class);
        assertThat(created).isNotNull();

        mockMvc.perform(get("/customers/" + user.id + "/tags/"+created.id)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        result = mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        foundTransaction = mapper.readValue(result.andReturn().getResponse().getContentAsString(), FoundTransaction.class);
        assertThat(foundTransaction.amount).isEqualTo(100.00);
        assertThat(foundTransaction.balanceAfter).isEqualTo(3000.00);
        assertThat(foundTransaction.balanceBefore).isEqualTo(3100.00);
        assertThat(foundTransaction.description).isEqualTo(newTransaction.description);
        assertThat(foundTransaction.tags.size()).isEqualTo(2);
        foundTransaction.tags.stream().forEach(t -> {
            if(t.id == 1){
                assertThat(t.name).isEqualTo(tag.name);
            }
            if(t.id == 2){
                assertThat(t.name).isEqualTo("ontheflytag");
            }
        });

        //remove tag from transaction
        mockMvc.perform(put("/customers/" + user.id + "/transactions/1/remove_tag/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        result = mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        foundTransaction = mapper.readValue(result.andReturn().getResponse().getContentAsString(), FoundTransaction.class);
        assertThat(foundTransaction.tags.size()).isEqualTo(1);
        assertThat(foundTransaction.tags.iterator().next().name).isEqualTo(tag.name);

        //add category to transaction
        Category category = new Category(user, "testCategory");
        Category savedCategory = this.categoryRepository.save(category);
        mockMvc.perform(put("/customers/" + user.id + "/transactions/1/add_category/" + savedCategory.id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());


        result = mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        foundTransaction = mapper.readValue(result.andReturn().getResponse().getContentAsString(), FoundTransaction.class);
        assertThat(foundTransaction.category.name).isEqualTo("testCategory");

        //add new category to transaction
        mockMvc.perform(put("/customers/" + user.id + "/transactions/1/addnew_category/" + "ontheflycategory")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        result = mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        foundTransaction = mapper.readValue(result.andReturn().getResponse().getContentAsString(), FoundTransaction.class);
        assertThat(foundTransaction.category.name).isEqualTo("ontheflycategory");

        mockMvc.perform(put("/customers/" + user.id + "/transactions/1/remove_category/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        result = mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        foundTransaction = mapper.readValue(result.andReturn().getResponse().getContentAsString(), FoundTransaction.class);
        assertThat(foundTransaction.category).isNull();

        //remove transaction
        mockMvc.perform(delete("/customers/" + user.id + "/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/customers/" + user.id + "/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        this.tagRepository.delete(this.tagRepository.findOne(1L)); //FIXME why cassacde all is not working on a customer?
        this.tagRepository.delete(this.tagRepository.findOne(2L)); //FIXME why cassacde all is not working on a customer?

        mockMvc.perform(delete("/customers/" + user.id + "/categories/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());//FIXME why cassacde all is not working on a customer?
        mockMvc.perform(delete("/customers/" + user.id + "/categories/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());//FIXME why cassacde all is not working on a customer?

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
