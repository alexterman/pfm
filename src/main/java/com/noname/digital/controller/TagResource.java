package com.noname.digital.controller;

import com.google.common.base.Preconditions;
import com.noname.digital.components.TagDAO;
import com.noname.digital.controller.rest.Created;
import com.noname.digital.controller.rest.FoundTag;
import com.noname.digital.controller.rest.ModifiedTag;
import com.noname.digital.controller.rest.NewTag;
import com.noname.digital.model.Category;
import com.noname.digital.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by alex on 1/8/16.
 */

@RestController
@RequestMapping("/customer/{id}")
public class TagResource {


    private Logger log = LoggerFactory.getLogger(TagResource.class);

    @Autowired
    private TagDAO tagDAO;


    @RequestMapping(method = RequestMethod.POST , path = "/tag")
    public ResponseEntity<Created> createTag(@RequestBody NewTag newTag) {

        log.debug("Invoked createCustomer [{}]", newTag);
        Tag saved = tagDAO.createTag(newTag);
        return new ResponseEntity(new Created(saved.id), HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET , path = "/customer/{id}/tag/")
    public ResponseEntity<FoundTag> getTag(@PathVariable("id") Long id) {

        log.debug("Invoked load customer id [{}]", id);
        Tag tag = this.tagDAO.getTag(id);

        FoundTag foundTag = toFoundTag(tag);
        return new ResponseEntity(foundTag, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.PUT , path = "/customer/{id}/tag")
    public ResponseEntity<Created> updateTag(@PathVariable ("id") Long id, @RequestBody ModifiedTag modifiedTag) {

        log.debug("Invoked updateTag " + modifiedTag);
        Preconditions.checkNotNull(modifiedTag.id, "Category ID can't be null");
        Preconditions.checkNotNull(modifiedTag.name, "Category name can't be null");

        this.tagDAO.updateTag(id, modifiedTag);
        return new ResponseEntity(new Created(modifiedTag.id), HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.DELETE , path = "/customer/{id}/tag/{tid}")
    public ResponseEntity<Category> deleteTag(@PathVariable("id") long id, @PathVariable("tid") long tid) {

        log.debug("Invoked delete tag id [{}]", tid);
        Preconditions.checkNotNull(id, "Customer ID can't be null");
        Preconditions.checkNotNull(tid, "Tag ID can't be null");

        this.tagDAO.deleteTag(id, tid);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }



    private FoundTag toFoundTag(Tag tag) {

        FoundTag foundTag = new FoundTag(tag.id, tag.name);
        return foundTag;
    }

}
