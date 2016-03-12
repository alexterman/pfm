package com.noname.digital.components;

import com.google.common.collect.Lists;
import com.noname.digital.controller.rest.NewCustomer;
import com.noname.digital.controller.rest.NewTransaction;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Transaction;
import com.noname.digital.repo.CategoryRepository;
import com.noname.digital.repo.CustomerRepository;
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
public class CustomerDAO {

    private Logger log = LoggerFactory.getLogger(CustomerDAO.class);

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    TransactionRepository transactionRepository;

    public long createCustomer (NewCustomer newCustomer){
        log.info("Requested to create customer: [{}]", newCustomer);

        Customer customer = new Customer(newCustomer.firstName, newCustomer.lastName);
        Customer saved = customerRepository.save(customer);
        return saved.id;
    }

    
    public List<Customer> listCustomers() {

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
