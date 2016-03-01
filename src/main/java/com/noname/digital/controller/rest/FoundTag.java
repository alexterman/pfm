package com.noname.digital.controller.rest;

import com.google.common.base.MoreObjects;

public class FoundTag {

    public long id;
    public String name;

    public FoundTag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }
}
