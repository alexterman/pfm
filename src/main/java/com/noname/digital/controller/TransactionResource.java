package com.noname.digital.controller;

import com.google.common.base.Preconditions;
import com.noname.digital.components.TransactionDAO;
import com.noname.digital.controller.rest.*;
import com.noname.digital.model.Tag;
import com.noname.digital.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex on 1/8/16.
 */

@RestController
@RequestMapping("/customers/{id}")
public class TransactionResource {


    private Logger log = LoggerFactory.getLogger(TransactionResource.class);

    @Autowired
    private TransactionDAO transactionDAO;


    @RequestMapping(method = RequestMethod.POST , path = "/transactions")
    public ResponseEntity<Created> createTransaction(
            @PathVariable("id") Long id, @RequestBody NewTransaction newTransaction) {

        log.debug("Invoked createCustomer [{}] [{}]", id, newTransaction);
        Transaction saved = transactionDAO.createTransaction(id, newTransaction);

        return new ResponseEntity(new Created(saved.id), HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/transactions/{tid}")
    public ResponseEntity<FoundTransaction> getTransaction(
            @PathVariable("id") Long id, @PathVariable("tid") Long tid) {

        log.debug("Invoked getTag id [{}] tid [{}]", id, tid);
        Transaction transaction = this.transactionDAO.getTransaction(id, tid);

        FoundTransaction foundTransaction = toFoundTransaction(transaction);
        return new ResponseEntity(foundTransaction, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.PUT , path = "/transactions")
    public ResponseEntity<Created> updateTransaction(
            @PathVariable("id") Long id, @RequestBody ModifiedTransaction modifiedTag) {

        log.debug("Invoked updateTransaction " + modifiedTag);

        this.transactionDAO.updateTransaction(id, modifiedTag);
        return new ResponseEntity(new Created(modifiedTag.id), HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.DELETE , path = "/customer/{id}/tag/{tid}")
    public ResponseEntity<?> deleteTag(@PathVariable("id") Long id, @PathVariable("tid") long tid) {

        log.debug("Invoked delete tag id [{}]", tid);
        Preconditions.checkNotNull(id, "Customer ID can't be null");
        Preconditions.checkNotNull(tid, "Tag ID can't be null");

        this.transactionDAO.deleteTransaction(id, tid);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    private FoundTransaction toFoundTransaction(Transaction transaction) {
        FoundTransaction foundTransaction= new FoundTransaction();
        foundTransaction.transactionId = transaction.id;
        foundTransaction.customerId = transaction.customer.id;
        foundTransaction.execution = transaction.execution;
        foundTransaction.description = transaction.description;
        foundTransaction.balanceBefore = transaction.balanceBefore;
        foundTransaction.balanceAfter = transaction.balanceAfter;
        foundTransaction.amount = transaction.amount;
        foundTransaction.category = new FoundCategory(transaction.category.id, transaction.category.name);
        foundTransaction.tags = convertTags(transaction.tags);
        return foundTransaction;
    }

    private Set<FoundTag> convertTags(Set<Tag> tags) {
        Set<FoundTag> foundTags = new HashSet<>();
        tags.stream().forEach( t -> foundTags.add(new FoundTag(t.id, t.name)));
        return foundTags;
    }
}
