package com.noname.digital.model;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long id;

    public String name;

    @OneToMany
    public Set<Transaction> transactions;

    @ManyToOne
    public Customer customer;

    protected Category() {}

    public Category(Customer customer, String name, Set<Transaction> transactions) {
        this.customer = customer;
        this.name = name;
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("transactions", transactions)
                .add("customer", customer)
                .toString();
    }

}
