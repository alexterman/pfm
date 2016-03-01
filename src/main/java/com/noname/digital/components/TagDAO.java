package com.noname.digital.components;

import com.noname.digital.controller.rest.ModifiedTag;
import com.noname.digital.controller.rest.NewTag;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Tag;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by alex on 3/1/16.
 */
@Component
public class TagDAO {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TagRepository tagRepository;

    public Tag getTag (long id){
        return this.tagRepository.findOne(id);
    }

    public Tag createTag (NewTag newTag){
        Customer customer = getCustomer(newTag.customerId);

        Tag tag = new Tag(customer,newTag.name);
        Tag saved = this.tagRepository.save(tag);
        return saved;
    }

    public Tag updateTag(Long customerId, ModifiedTag modifiedTag) {
        getCustomer(customerId);
        Tag toBeUpdated = this.tagRepository.findOne(modifiedTag.id);
        checkNotNull(toBeUpdated, "The tag with id " + modifiedTag.id + " not found");
        toBeUpdated.name = modifiedTag.name;
        Tag updated = this.tagRepository.save(toBeUpdated);
        return updated;
    }


    public void deleteTag(Long customerId, Long tagId) {
        getCustomer(customerId);
        this.tagRepository.delete(tagId);
    }

    private Customer getCustomer(Long id) {
        Customer customer = this.customerRepository.findOne(id);
        checkNotNull(customer,"No customer found for id="+id);
        return customer;
    }

}
