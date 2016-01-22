package com.noname.digital.components;

import com.google.common.collect.Lists;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Tag;
import com.noname.digital.model.Transaction;
import com.noname.digital.repo.CategoryRepository;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TagRepository;
import com.noname.digital.repo.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 1/9/16.
 */

@Component
public class PFMJpaDao {

    private Logger log = LoggerFactory.getLogger(PFMJpaDao.class);

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    TransactionRepository transactionRepository;
    
    @Autowired
    TagRepository tagRepository;


    public void createCustomer (Customer customer){
        if(log.isDebugEnabled()){
            log.info("Requested to create custome: " + customer);
        }
        customerRepository.save(customer);
    }

    public void createCategory (Category category){
        if(log.isDebugEnabled()){
            log.info("Requested to create category: " + category);
        }
        categoryRepository.save(category);
    }
    
    
    public void createTag (Tag tag){
        if(log.isDebugEnabled()){
            log.info("Requested to create tag: " + tag);
        }
        tagRepository.save(tag);
    }
    
    
    public void createTransaction (Transaction transaction){
        if(log.isDebugEnabled()){
            log.info("Requested to create transaction: " + transaction);
        }
        transactionRepository.save(transaction);
    }

    
    public List<Customer> listCustomers() {

        // fetch all customers
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        Iterable<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
                log.info(customer.toString());
        }

        return Lists.newArrayList(customers);

    }

}
