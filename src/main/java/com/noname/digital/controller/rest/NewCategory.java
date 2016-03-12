package com.noname.digital.controller.rest;

import com.google.common.base.MoreObjects;

/**
 * Created by alex on 2/6/16.
 */
public class NewCategory {

    public String name;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
