package com.noname.digital.controller;

import com.noname.digital.components.CustomerDAO;
import com.noname.digital.controller.rest.*;
import com.noname.digital.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 1/8/16.
 */

@RestController
public class CustomerResource {


    private Logger log = LoggerFactory.getLogger(CustomerResource.class);

    @Autowired
    private CustomerDAO customerDAO;

    @RequestMapping(method = RequestMethod.GET , path = "/customers")
    public List<Customer> listCustomers() {
        return customerDAO.listCustomers();
    }

    @RequestMapping(method = RequestMethod.POST , path = "/customers")
    public ResponseEntity<Created> createCustomer(@RequestBody NewCustomer customer) {

        log.debug("Invoked createCustomer [{}]", customer);
        long id = customerDAO.createCustomer(customer);
        return new ResponseEntity(new Created(id), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET , path = "/customers/{id}")
    public ResponseEntity<FoundCustomer> getCustomer(@PathVariable("id") Long id) {

        log.debug("Invoked load customer id [{}]", id);
        Customer customer = customerDAO.getCustomer(id);

        FoundCustomer foundCustomer = toFoundCustomer(customer);
        return new ResponseEntity(foundCustomer, HttpStatus.OK);
    }


    private static FoundCustomer toFoundCustomer(Customer customer){
        FoundCustomer foundCustomer = new FoundCustomer();
        foundCustomer.firstName = customer.firstName;
        foundCustomer.lastName = customer.lastName;
        foundCustomer.id = customer.id;
        foundCustomer.categories = customer.categories.stream().map(c -> new FoundCategory(c.id, c.name)).collect(Collectors.toSet());
        foundCustomer.tags = customer.tags.stream().map(t -> new FoundTag(t.id, t.name)).collect(Collectors.toSet());
        return foundCustomer;
    }

}
