package com.noname.digital.model;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long id;
    public String firstName;
    public String lastName;

    @OneToMany
    public Set<Category> categories;

    @OneToMany
    public Set<Transaction> transactions;

    @OneToMany
    public Set<Tag> tags;


    protected Customer() {}

    public Customer(String firstName,
                    String lastName,
                    Set<Category> categories,
                    Set<Transaction> transactions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.categories = categories;
        this.transactions = transactions;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("categories", categories)
                .add("transactions", transactions)
                .toString();
    }

}
