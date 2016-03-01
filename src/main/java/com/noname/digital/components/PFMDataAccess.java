package com.noname.digital.components;

import com.google.common.collect.Lists;
import com.noname.digital.controller.rest.*;
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

import java.util.List;

/**
 * Created by alex on 1/9/16.
 */

@Component
public class PFMDataAccess {

    private Logger log = LoggerFactory.getLogger(PFMDataAccess.class);

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    TransactionRepository transactionRepository;
    
    @Autowired
    TagRepository tagRepository;


    public long createCustomer (NewCustomer newCustomer){
        log.info("Requested to create custome: [{}]", newCustomer);

        Customer customer = new Customer(newCustomer.firstName, newCustomer.lastName);
        Customer saved = customerRepository.save(customer);
        return saved.id;
    }

    public long createCategory (NewCategory newCategory){
        log.info("Requested to create category: [{}]", newCategory);

        Customer customer = this.customerRepository.findOne(newCategory.customerId);
        Transaction transaction = this.transactionRepository.findOne(newCategory.transactionId);
        Category category = new Category(customer,newCategory.name, transaction);
        Category created = categoryRepository.save(category);
        return created.id;
    }

    public void updateCategory(ModifiedCategory modifiedCategory){

        Category category = this.categoryRepository.findOne(modifiedCategory.id);
        category.name = modifiedCategory.name;

        this.categoryRepository.save(category);

    }

    public Category removeCategory(long id){

        Category toRemove = this.categoryRepository.findOne(id);
        this.categoryRepository.delete(toRemove);

        return toRemove;
    }


    
    public void createTransaction (NewTransaction newTransaction){
        if(log.isDebugEnabled()){
            log.info("Requested to create transaction: [{}]", newTransaction);
        }

        Customer customer = this.customerRepository.findOne(newTransaction.customerId);

        Transaction transaction = new Transaction(
                customer,
                newTransaction.execution,
                newTransaction.description,
                newTransaction.balanceBefore,
                newTransaction.balanceAfter,
                newTransaction.ammount
                );
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

    public Customer getCustomer(long id) {
        return this.customerRepository.findOne(id);
    }
}
