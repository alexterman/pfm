package com.noname.digital.components;

import com.noname.digital.controller.rest.ModifiedTag;
import com.noname.digital.controller.rest.NewTag;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Tag;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by alex on 3/1/16.
 */
@Component
public class TagDAO {

    private Logger log = LoggerFactory.getLogger(TagDAO.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TagRepository tagRepository;

    public Tag getTag (long customerId, long tagId){
        if (validateCustomer(customerId) == null){
            return null;
        }
        return this.tagRepository.findOne(tagId);
    }


    public Tag createTag (Long customerId, NewTag newTag){
        Customer customer = validateCustomer(customerId);
        if (customer == null){
            return null;
        }
        Tag tag = new Tag(customer,newTag.name);
        Tag saved = this.tagRepository.save(tag);
        return saved;
    }

    public Tag updateTag(Long customerId, ModifiedTag modifiedTag) {
        Customer customer = validateCustomer(customerId);
        if (customer == null){
            return null;
        }
        Tag toBeUpdated = this.tagRepository.findOne(modifiedTag.id);
        checkNotNull(toBeUpdated, "The tag with id " + modifiedTag.id + " not found");
        toBeUpdated.name = modifiedTag.name;
        Tag updated = this.tagRepository.save(toBeUpdated);
        return updated;
    }


    public void deleteTag(Long customerId, Long tagId) {
        Customer customer = validateCustomer(customerId);
        if (customer == null){
            return ;
        }
        this.tagRepository.delete(tagId);
    }

    private Customer getCustomer(Long id) {
        Customer customer = this.customerRepository.findOne(id);
        return customer;
    }

    private Customer validateCustomer(long customerId) {
        Customer customer = getCustomer(customerId);
        if(customer== null){
            log.error("No customer found with id = " + customerId);
            return null;
        }
        return customer;
    }
}
