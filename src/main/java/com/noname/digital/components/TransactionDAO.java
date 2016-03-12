package com.noname.digital.components;

import com.noname.digital.controller.rest.NewTransaction;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Tag;
import com.noname.digital.model.Transaction;
import com.noname.digital.repo.CategoryRepository;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TagRepository;
import com.noname.digital.repo.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by alex on 3/1/16.
 */
@Component
public class TransactionDAO {


    private Logger log = LoggerFactory.getLogger(TransactionDAO.class);

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

        Category category = this.categoryRepository.findOne(categoryId);
        Transaction transaction = this.transactionRepository.findOne(transactionId);

        if(category.customer.id == customerId && transaction.customer.id == customerId){
            transaction.category = category;
            this.transactionRepository.save(transaction);
        }else{
            log.error("addCategoryToTransaction failed, the customer id [{}] doesn't match customer id in category id [{}] or/and transaction id [{}]"
                    , customerId, categoryId, transactionId);
        }
    }


    public Category addNewCategoryToTransaction(Long customerId, Long transactionId, String categoryName) {

        Customer customer = getCustomer(customerId);
        Transaction transaction = this.transactionRepository.findOne(transactionId);

        if(transaction.customer.id != customerId){
            throw new RuntimeException("addNewCategoryToTransaction failed, transaction is not belongs to a customer");
        }

        Category category = new Category(customer, categoryName);
        Category saved = this.categoryRepository.save(category);

        transaction.category = category;
        this.transactionRepository.save(transaction);

        return saved;
    }


    public void removeCategoryFromTransaction(Long customerId, Long transactionId, Long categoryId) {

        Category category = this.categoryRepository.findOne(categoryId);
        Transaction transaction = this.transactionRepository.findOne(transactionId);

        if(category.customer.id == customerId && transaction.customer.id == customerId){
            transaction.category = null;
            this.transactionRepository.save(transaction);
        }else{
            log.error("addCategoryToTransaction failed, the customer id [{}] doesn't match customer id in category id [{}] or/and transaction id [{}]"
                    , customerId, categoryId, transactionId);
        }
    }

    public void addTagToTransaction(Long customerId, Long transactionId, Long tagId) {

        Transaction transaction = this.transactionRepository.findOne(transactionId);
        Tag tag = this.tagRepository.findOne(tagId);

        if(transaction.customer.id == customerId && tag.customer.id == customerId){
            tag.transactions.add(transaction);
            //transaction.tags.add(tag);

            this.transactionRepository.save(transaction);
        }else{
            throw new RuntimeException ( //TODO maybe change to invalid arguments exception ...
                    "addTagToTransaction failed, the customerId " +
                            "["+customerId+"], transactionId ["+transactionId+"], tagId ["+tagId+"] doesn't match");
        }
    }

    public void removeTagFromTransaction(Long customerId, Long transactionId, Long tagId) {

        Transaction transaction = this.transactionRepository.findOne(transactionId);
        Tag tag = this.tagRepository.findOne(tagId);

        if(transaction.customer.id == customerId && tag.customer.id == customerId){
            tag.transactions.remove(transaction);
            //transaction.tags.remove(tag);
            this.transactionRepository.save(transaction);
        }else{
            log.error(
                    "removeTagFromTransaction failed, the customerId [{}], transactionId [{}], tagId [{}] not match ",
                    customerId, transactionId, tagId);
        }
    }


    public Tag addNewTagToTransaction(Long customerId, Long transactionId, String tagName) {

        Customer customer = getCustomer(customerId);
        Transaction transaction = this.transactionRepository.findOne(transactionId);

        if(transaction.customer.id != customerId){
            throw new RuntimeException("addNewTagToTransaction failed, transaction is not belongs to a customer");
        }

        Tag saved = createNewTag(tagName, customer);

        //transaction.tags.add(saved);
        saved.transactions.add(transaction);
        this.transactionRepository.save(transaction);

        return saved;
    }

    public void deleteTransaction (Long customerId, Long transactionId) {
        Transaction transactionToDelete  = this.transactionRepository.findOne(transactionId);

        if(transactionToDelete.customer.id == customerId){
            Set<Tag> tags = transactionToDelete.tags;
            tags.stream().forEach(t -> t.transactions.remove(transactionToDelete));
            this.transactionRepository.delete(transactionToDelete);
        }
    }

    private Customer getCustomer(Long id) {
        Customer customer = this.customerRepository.findOne(id);
        checkNotNull(customer,"No customer found for id="+id);
        return customer;
    }


    private Tag createNewTag(String tagName, Customer customer) {
        Tag tag = new Tag();
        tag.customer = customer;
        tag.name = tagName;
        tag.transactions = new HashSet<>();
        return this.tagRepository.save(tag);
    }

    public List<Transaction> getTransactions(Long id) {
        getCustomer(id);

        return this.transactionRepository.findByCustomer_Id(id);
    }
}
