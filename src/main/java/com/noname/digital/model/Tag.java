package com.noname.digital.model;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long id;

    @ManyToOne
    public Customer customer;

    @ManyToMany
    public Set<Transaction> transactions;

    public String name;

    public Tag(){}

    public Tag(Customer customer, String name) {
        this.customer = customer;
        this.name = name;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("customer", customer.id)
                .add("name", name)
                .toString();
    }
}
