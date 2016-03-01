package com.noname.digital.controller;

import com.google.common.base.Preconditions;
import com.noname.digital.components.CategoryDAO;
import com.noname.digital.components.TagDAO;
import com.noname.digital.controller.rest.*;
import com.noname.digital.model.Category;
import com.noname.digital.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * Created by alex on 1/8/16.
 */

@RestController
@RequestMapping("/customer/{id}")
public class CategoryResource {


    private Logger log = LoggerFactory.getLogger(CategoryResource.class);

    @Autowired
    private CategoryDAO categoryDAO;

    @RequestMapping(method = RequestMethod.POST , path = "/category")
    public ResponseEntity<Created> createCategory(
            @PathVariable ("id") Long customerId, @RequestBody NewCategory newCategory) {

        log.debug("Invoked createCategory " + newCategory + " for customer " + customerId);
        Preconditions.checkNotNull(customerId, "Customer id can't be null");
        Preconditions.checkNotNull(newCategory, "Category can't be null");
        Preconditions.checkNotNull(newCategory.name, "Category name can't be null");

        Category created = this.categoryDAO.createCategory(customerId, newCategory);
        return new ResponseEntity(new Created(created.id), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/category")
    public ResponseEntity<Created> updateCategory(
            @PathVariable ("id") Long customerId, @RequestBody ModifiedCategory modifiedCategory) {

        log.debug("Invoked updateCategory " + modifiedCategory);
        Preconditions.checkNotNull(customerId, "Customer ID can't be null");
        Preconditions.checkNotNull(modifiedCategory.id, "Category ID can't be null");
        Preconditions.checkNotNull(modifiedCategory.name, "Category name can't be null");

        this.categoryDAO.updateCategory(customerId, modifiedCategory);
        return new ResponseEntity(new Created(modifiedCategory.id), HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.DELETE , path = "/category/{cid}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable ("id") Long customerId, @PathVariable ("cid") Long cid) {

        log.debug("Invoked deleteCategory category [{}]", cid);
        Preconditions.checkNotNull(cid, "Category ID can't be null");

        this.categoryDAO.removeCategory(customerId, cid);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


}
