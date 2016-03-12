package com.noname.digital.controller.rest;

import com.google.common.base.MoreObjects;

import java.util.Date;

/**
 * Created by alex on 2/6/16.
 */
public class NewTransaction {

    public Date execution;
    public String description;
    public double balanceBefore;
    public double balanceAfter;
    public double amount;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("execution", execution)
                .add("description", description)
                .add("balanceBefore", balanceBefore)
                .add("balanceAfter", balanceAfter)
                .add("amount", amount)
                .toString();
    }
}
