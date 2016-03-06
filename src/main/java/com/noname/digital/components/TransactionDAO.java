package com.noname.digital.components;

import com.noname.digital.controller.rest.*;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Tag;
import com.noname.digital.model.Transaction;
import com.noname.digital.repo.CategoryRepository;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TagRepository;
import com.noname.digital.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by alex on 3/1/16.
 */
@Component
public class TransactionDAO {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    public Transaction getTransaction (long customerId, long tagId){
        getCustomer(customerId);
        return this.transactionRepository.findOne(tagId);
    }

    public Transaction createTransaction (Long customerId, NewTransaction newTransaction){
        Customer customer = getCustomer(customerId);

        Transaction transaction = new Transaction(
                customer,
                newTransaction.execution,
                newTransaction.description,
                newTransaction.balanceBefore,
                newTransaction.balanceAfter,
                newTransaction.amount);

        Transaction saved = this.transactionRepository.save(transaction);
        return saved;
    }

    public void addCategoryToTransaction(Long customerId, Long categoryId, Long transactionId) {
        Customer customer = getCustomer(customerId);

        isCustomerOwnerOfCategory(categoryId, customer);
        isCustomerOwnerOfTransaction(transactionId, customer);

        Category category = this.categoryRepository.findOne(categoryId);
        Transaction transaction = this.transactionRepository.findOne(transactionId);

        transaction.category = category;
        this.transactionRepository.save(transaction);
    }

    public void addTagsToTransaction(Long customerId, Long transactionId, List<Long> tags) {
        Customer customer = getCustomer(customerId);

        isCustomerOwnerOfTags(tags, customer);
        isCustomerOwnerOfTransaction(transactionId, customer);

        Transaction transaction = this.transactionRepository.findOne(transactionId);
        final Set<Tag> resolved = new HashSet<>();
        tags.stream().forEach(tId -> resolved.add(this.tagRepository.findOne(tId)));

        Set<Tag> transactionTags = transaction.tags;
        if(transactionTags == null){
           transactionTags = new HashSet<>();
        }
        transactionTags.addAll(resolved);
        this.transactionRepository.save(transaction);
    }


    public void updateTransaction(Long customerId, ModifiedTransaction modifiedTransaction) {
        Customer customer = getCustomer(customerId);
        isCustomerOwnerOfTransaction(modifiedTransaction.id, customer);

        Transaction transaction = this.transactionRepository.findOne(modifiedTransaction.id);
        updateTransaction(transaction, modifiedTransaction, customer);
        this.transactionRepository.save(transaction);

    }


    public void deleteTransaction (Long customerId, Long transactionId) {
        Customer customer = getCustomer(customerId);
        isCustomerOwnerOfTransaction(transactionId, customer);

        this.transactionRepository.delete(transactionId);
    }


    private void updateTransaction(
            Transaction transaction, ModifiedTransaction modifiedTransaction, Customer customer){
        transaction.category = convertCategory(modifiedTransaction.category, customer);
        transaction.tags = convertTags(modifiedTransaction.tags, customer);
        transaction.description = modifiedTransaction.description;

    }

    private Category convertCategory(FoundCategory category, Customer customer) {

        Category c = this.categoryRepository.findOne(category.id);
        if(c == null){
            c = this.categoryRepository.save(new Category(customer, category.name));
        }
        return c;
    }

    private Set<Tag> convertTags(Set<FoundTag> tags, Customer customer) {
        Set<Tag> converted = new HashSet<>();
        tags.stream().forEach(foundTag -> {
            Tag existing = this.tagRepository.findOne(foundTag.id);
            if(existing == null){
                existing = this.tagRepository.save(new Tag(customer, foundTag.name));
            }
            converted.add(existing);
        });
        return converted;
    }


    private void isCustomerOwnerOfTransaction(Long transactionId, Customer customer) {
        checkArgument(
                customer.transactions.stream().
                        filter(t->t.id == transactionId).
                        collect(Collectors.toList()).size() == 1,
                "Customer with id [{}] is not own Transaction with id [{}]", customer.id, transactionId);
    }

    private void isCustomerOwnerOfCategory(Long categoryId, Customer customer) {
        checkArgument(
                customer.categories.stream().
                        filter(c->c.id == categoryId).
                        collect(Collectors.toList()).size() == 1,
                "Customer with id [{}] is not own Category with id [{}]", customer.id, categoryId);
    }

    private void isCustomerOwnerOfTags(List<Long> tags, Customer customer) {
        checkArgument(
                customer.tags.stream().
                        map(t -> t.id).
                        collect(Collectors.toSet()).containsAll(tags),
                "Customer with id [{}] is not own one or more Tags with ids [{}]", customer.id, tags);
    }

    private Customer getCustomer(Long id) {
        Customer customer = this.customerRepository.findOne(id);
        checkNotNull(customer,"No customer found for id="+id);
        return customer;
    }

}
