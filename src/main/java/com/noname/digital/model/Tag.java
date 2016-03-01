package com.noname.digital.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public long id;

    @ManyToOne
    private Customer customer;

    @ManyToMany
    public Set<Transaction> transactions;

    public String name;

    public Tag(){}
    
    public Tag(Customer customer, String name) {
        this.customer = customer;
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
