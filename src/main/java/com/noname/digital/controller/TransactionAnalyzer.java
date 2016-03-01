package com.noname.digital.controller;

import com.google.common.base.Preconditions;
import com.noname.digital.components.PFMDataAccess;
import com.noname.digital.controller.rest.*;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 1/8/16.
 */

@RestController
public class TransactionAnalyzer {


    private Logger log = LoggerFactory.getLogger(TransactionAnalyzer.class);

    @Autowired
    private PFMDataAccess pfmDataAccess;

    @RequestMapping(method = RequestMethod.GET , path = "/customer")
    public List<Customer> listCustomers() {
        return pfmDataAccess.listCustomers();

    }

    @RequestMapping(method = RequestMethod.POST , path = "/customer")
    public ResponseEntity<Created> createCustomer(@RequestBody NewCustomer customer) {

        log.debug("Invoked createCustomer [{}]", customer);
        long id = pfmDataAccess.createCustomer(customer);
        return new ResponseEntity(new Created(id), HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET , path = "/customer/{id}")
    public ResponseEntity<FoundCustomer> getCustomer(@PathVariable("id") Long id) {

        log.debug("Invoked load customer id [{}]", id);
        Customer customer = pfmDataAccess.getCustomer(id);

        FoundCustomer foundCustomer = toFoundCustomer(customer);
        return new ResponseEntity(foundCustomer, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST , path = "/category")
    public ResponseEntity<Created> createCategory(@RequestBody NewCategory newCategory) {

        log.debug("Invoked createCategory " + newCategory);
        Preconditions.checkNotNull(newCategory, "Category can't be null");
        Preconditions.checkNotNull(newCategory.name, "Category name can't be null");

        long id = pfmDataAccess.createCategory(newCategory);
        return new ResponseEntity(new Created(id), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/category")
    public ResponseEntity<Created> updateCategory(@RequestBody ModifiedCategory modifiedCategory) {

        log.debug("Invoked createCategory " + modifiedCategory);
        Preconditions.checkNotNull(modifiedCategory.id, "Category ID can't be null");
        Preconditions.checkNotNull(modifiedCategory.name, "Category name can't be null");

        pfmDataAccess.updateCategory(modifiedCategory);
        return new ResponseEntity(new Created(modifiedCategory.id), HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.DELETE , path = "/category/{id}")
    public ResponseEntity<Category> deleteCategory(@PathParam("id") long id) {

        log.debug("Invoked delete category [{}]", id);
        Preconditions.checkNotNull(id, "Category ID can't be null");

        Category removed = pfmDataAccess.removeCategory(id);
        return new ResponseEntity(removed, HttpStatus.ACCEPTED);
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
