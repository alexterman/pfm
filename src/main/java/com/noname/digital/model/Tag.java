package com.noname.digital.model;

import com.google.common.base.MoreObjects;
import org.omg.CORBA.TRANSACTION_MODE;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    public Set<Transaction> transactions;

    public String name;

    public Tag(Customer customer, Transaction transaction, String name) {
        this.customer = customer;
        this.transactions.add(transaction);
        this.name = name;
    }
//
//    @Override
//    public String toString() {
//        return MoreObjects.toStringHelper(this)
//                .add("id", id)
//                .add("customer", customer.id)
//                .add("transactions", transactions.stream().map(transaction -> transaction.id).collect(Collectors.toList()))
//                .add("name", name)
//                .toString();
//    }
}
