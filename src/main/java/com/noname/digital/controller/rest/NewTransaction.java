package com.noname.digital.controller.rest;

import com.google.common.base.MoreObjects;

import java.util.Date;

/**
 * Created by alex on 2/6/16.
 */
public class NewTransaction {

    public long customerId;
    public Date execution;
    public String description;
    public long balanceBefore;
    public long balanceAfter;
    public long ammount;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("customerId", customerId)
                .add("execution", execution)
                .add("description", description)
                .add("balanceBefore", balanceBefore)
                .add("balanceAfter", balanceAfter)
                .add("amount", ammount)
                .toString();
    }
}
