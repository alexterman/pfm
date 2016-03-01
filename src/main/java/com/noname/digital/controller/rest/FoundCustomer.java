package com.noname.digital.controller.rest;

import com.google.common.base.MoreObjects;

import java.util.Set;

/**
 * Created by alex on 3/1/16.
 */
public class FoundCustomer {

    public Long id;
    public String firstName;
    public String lastName;
    public Set<FoundCategory> categories;
    public Set<FoundTag> tags;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("categories", categories)
                .add("tags", tags)
                .toString();
    }
}
