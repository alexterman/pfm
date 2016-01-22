package com.noname.digital.model;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    public Set<Tag> transactions;

    public String name;

    protected Tag() {}

    public Tag(Customer customer, Set<Tag> transactions, String name) {
        this.customer = customer;
        this.transactions = transactions;
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("customer", customer)
                .add("transactions", transactions)
                .add("name", name)
                .toString();
    }
}
