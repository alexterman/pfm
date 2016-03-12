package com.noname.digital.controller;

import com.google.common.base.Preconditions;
import com.noname.digital.components.TransactionDAO;
import com.noname.digital.controller.rest.*;
import com.noname.digital.model.Category;
import com.noname.digital.model.Tag;
import com.noname.digital.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

        log.debug("Invoked get transaction id [{}] tid [{}]", id, tid);
        Transaction transaction = this.transactionDAO.getTransaction(id, tid);

        if(transaction == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        FoundTransaction foundTransaction = toFoundTransaction(transaction);
        return new ResponseEntity(foundTransaction, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/transactions")
    public ResponseEntity<FoundTransaction> getTransactions(
            @PathVariable("id") Long id) {

        log.debug("Invoked list all transactions id [{}] tid [{}]", id);
        List<Transaction> transactions = this.transactionDAO.getTransactions(id);

        List<FoundTransaction> foundTransactions = toFoundTransactions(transactions);
        return new ResponseEntity(foundTransactions, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT , path = "/transactions/{tid}/add_tag/{tagid}")
    public ResponseEntity<HttpStatus> addTagToTransaction(
            @PathVariable("id") Long id, @PathVariable("tid") Long tid, @PathVariable ("tagid") Long tagid) {

        log.debug("Invoked addTagToTransaction {} {} {}",id, tid, tagid);

        try {
            this.transactionDAO.addTagToTransaction(id, tid, tagid);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error("Failed to invoke addTagToTransaction ", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/transactions/{tid}/remove_tag/{tagid}")
    public ResponseEntity<HttpStatus> removeTagFromTransaction(
            @PathVariable("id") Long id, @PathVariable("tid") Long tid, @PathVariable ("tagid") Long tagid) {

        log.debug("Invoked removeTagFromTransaction {} {} {}",id, tid, tagid);

        try {
            this.transactionDAO.removeTagFromTransaction(id, tid, tagid);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            log.error("Failed to invoke removeTagFromTransaction ", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(method = RequestMethod.PUT , path = "/transactions/{tid}/addnew_tag/{tagname}")
    public ResponseEntity<HttpStatus> addNewTagToTransaction(
            @PathVariable("id") Long id, @PathVariable("tid") Long tid, @PathVariable ("tagname") String tagname) {

        log.debug("Invoked addNewTagToTransaction {} {} {}",id, tid, tagname);

        try {
            Tag tag = this.transactionDAO.addNewTagToTransaction(id, tid, tagname);
            return new ResponseEntity(new Created(tag.id), HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Failed to invoke addNewTagToTransaction ", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/transactions/{tid}/remove_category/{cid}")
    public ResponseEntity<HttpStatus> removeCategoryFromTransaction(
            @PathVariable("id") Long id, @PathVariable("tid") Long tid, @PathVariable ("cid") Long cId) {

        log.debug("Invoked removeCategoryFromTransaction {} {} {}",id, tid, cId);

        try {
            this.transactionDAO.removeCategoryFromTransaction(id, tid, cId);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            log.error("Failed to invoke removeCategoryFromTransaction ", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/transactions/{tid}/add_category/{cid}")
    public ResponseEntity<HttpStatus> addCategoryToTransaction(
            @PathVariable("id") Long id, @PathVariable("tid") Long tid, @PathVariable ("cid") Long cId) {

        log.debug("Invoked addCategoryToTransaction {} {} {}",id, tid, cId);

        try {
            this.transactionDAO.addCategoryToTransaction(id, tid, cId);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            log.error("Failed to invoke addCategoryToTransaction ", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT , path = "/transactions/{tid}/addnew_category/{name}")
    public ResponseEntity<HttpStatus> addNewCategoryToTransaction(
            @PathVariable("id") Long id, @PathVariable("tid") Long tid, @PathVariable ("name") String name) {

        log.debug("Invoked addNewCategoryToTransaction {} {} {}",id, tid, name);

        try {
            Category category = this.transactionDAO.addNewCategoryToTransaction(id, tid, name);
            return new ResponseEntity(new Created(category.id), HttpStatus.OK);
        }catch (Exception e){
            log.error("Failed to invoke addNewCategoryToTransaction ", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(method = RequestMethod.DELETE , path = "/transactions/{tid}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") Long id, @PathVariable("tid") long tid) {

        log.debug("Invoked deleteTransaction id [{}]", tid);
        Preconditions.checkNotNull(id, "Customer ID can't be null");
        Preconditions.checkNotNull(tid, "Transaction ID can't be null");

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
        if(transaction.category != null){
            foundTransaction.category = new FoundCategory(transaction.category.id, transaction.category.name);
        }
        foundTransaction.tags = convertTags(transaction.tags);
        return foundTransaction;
    }

    private List<FoundTransaction> toFoundTransactions (List<Transaction> transactions){

        List<FoundTransaction> result = new ArrayList<>();
        transactions.stream().forEach(t -> result.add(toFoundTransaction(t)));
        return result;
    }

    private Set<FoundTag> convertTags(Set<Tag> tags) {
        Set<FoundTag> foundTags = new HashSet<>();
        tags.stream().forEach( t -> foundTags.add(new FoundTag(t.id, t.name)));
        return foundTags;
    }
}
