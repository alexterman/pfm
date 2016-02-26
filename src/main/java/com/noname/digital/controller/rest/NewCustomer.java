package com.noname.digital.controller.rest;

import com.google.common.base.MoreObjects;

/**
 * Created by alex on 2/6/16.
 */
public class NewCustomer {

    public String firstName;
    public String lastName;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .toString();
    }
}
