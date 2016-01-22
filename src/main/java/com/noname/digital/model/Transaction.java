package com.noname.digital.model;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long id;

    @ManyToOne
    public Category category;

    @ManyToOne
    public Customer customer;

    @ManyToMany
    public Set<Tag> tags;

    public Date execution;
    
    public String description;

    public long balanceBefore;

    public long balanceAfter;

    public long ammount;

    protected Transaction() {}


    public Transaction(Category category, Customer customer, Date execution, String description,
                       long balanceBefore, long balanceAfter, long amount) {
        this.category = category;
        this.customer = customer;
        this.execution = execution;
        this.description = description;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.ammount = amount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("category", category)
                .add("customer", customer)
                .add("tags", tags)
                .add("execution", execution)
                .add("description", description)
                .add("balanceBefore", balanceBefore)
                .add("balanceAfter", balanceAfter)
                .add("ammount", ammount)
                .toString();
    }
}
