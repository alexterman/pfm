package com.noname.digital.controller.rest;

import com.google.common.base.MoreObjects;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 2/6/16.
 */
public class FoundTransaction {

    public long transactionId;
    public long customerId;
    public Date execution;
    public String description;
    public long balanceBefore;
    public long balanceAfter;
    public long amount;

    public FoundCategory category;
    public Set<FoundTag> tags;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("transactionId", transactionId)
                .add("customerId", customerId)
                .add("execution", execution)
                .add("description", description)
                .add("balanceBefore", balanceBefore)
                .add("balanceAfter", balanceAfter)
                .add("amount", amount)
                .add("category", category.name)
                .add("tags", tags.stream().map(t->t.name).collect(Collectors.toSet()))
                .toString();
    }
}
