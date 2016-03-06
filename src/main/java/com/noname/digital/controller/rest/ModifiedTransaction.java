package com.noname.digital.controller.rest;

import java.util.Date;
import java.util.Set;

/**
 * Created by alex on 3/3/16.
 */
public class ModifiedTransaction {

    public Long id;
    public Date execution;
    public String description;
    public long balanceBefore;
    public long balanceAfter;
    public long amount;

    public FoundCategory category;
    public Set<FoundTag> tags;

}
