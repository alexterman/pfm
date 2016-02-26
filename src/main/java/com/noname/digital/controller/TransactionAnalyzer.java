package com.noname.digital.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.noname.digital.components.PFMJpaDao;
import com.noname.digital.controller.rest.NewCategory;
import com.noname.digital.controller.rest.NewCustomer;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by alex on 1/8/16.
 */

@RestController
public class TransactionAnalyzer {


    private Logger log = LoggerFactory.getLogger(TransactionAnalyzer.class);
    @Autowired
    private PFMJpaDao pfmJpaDao;

    @RequestMapping(method = RequestMethod.GET , path = "/customer")
    public List<Customer> listCustomers() {

        try {
            return pfmJpaDao.listCustomers();
        } catch (Exception e) {
            throw new RuntimeException("Error invoking DAO " ,e);
        }

    }

    @RequestMapping(method = RequestMethod.POST , path = "/customer")
    public void createCustomer(@RequestBody NewCustomer customer) {

        log.debug("Invoked createCustomer " + customer);
        try {
            pfmJpaDao.createCustomer(customer);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking DAO " ,e);
        }
    }

    @RequestMapping(method = RequestMethod.POST , path = "/category")
    public ResponseEntity<?> createCategory(@RequestBody NewCategory newCategory) {

        log.debug("Invoked createCategory " + newCategory);
        Preconditions.checkNotNull(newCategory, "Category can't be null");
        Preconditions.checkNotNull(newCategory.name, "Category name can't be null");

        try {

            pfmJpaDao.createCategory(newCategory);
            return new ResponseEntity(HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException("Error invoking DAO " ,e);
        }
    }



}
