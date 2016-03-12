package com.noname.digital.model;

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

    @ManyToMany(mappedBy = "transactions")
    public Set<Tag> tags;

    public Date execution;
    
    public String description;

    public double balanceBefore;

    public double balanceAfter;

    public double amount;

    protected Transaction() {}


    public Transaction(Customer customer, Category category, Date execution, String description,
                       double balanceBefore, double balanceAfter, double amount) {
        this.category = category;
        this.customer = customer;
        this.execution = execution;
        this.description = description;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.amount = amount;
    }


    public Transaction(Customer customer, Date execution, String description,
                       double balanceBefore, double balanceAfter, double amount) {

        this.customer = customer;
        this.execution = execution;
        this.description = description;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.amount = amount;
    }
}
