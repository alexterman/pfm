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

    @OneToMany(mappedBy = "category")
    public Set<Transaction> transactions;

    @ManyToOne
    public Customer customer;

    protected Category() {}

    public Category(Customer customer, String name) {
        this.customer = customer;
        this.name = name;
    }

    public Category(Customer customer, String name, Transaction transaction) {
        this.customer = customer;
        this.name = name;
        this.transactions.add(transaction);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("customer", customer.id)
                .toString();
    }

}
