package com.noname.digital.components;

import com.noname.digital.controller.rest.ModifiedCategory;
import com.noname.digital.controller.rest.NewCategory;
import com.noname.digital.model.Category;
import com.noname.digital.model.Customer;
import com.noname.digital.model.Transaction;
import com.noname.digital.repo.CategoryRepository;
import com.noname.digital.repo.CustomerRepository;
import com.noname.digital.repo.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by alex on 3/1/16.
 */
@Component
public class CategoryDAO {

    private Logger log = LoggerFactory.getLogger(CategoryDAO.class);


    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public Optional<Category> getCategory (Long customerId, long categoryId){
        Customer customer = getCustomer(customerId);
        if(!isCategoryBelongsToCustomer(categoryId, customer)){
            return Optional.empty();
        }

        return Optional.of(this.categoryRepository.findOne(categoryId));
    }


    public Category createCategory (Long customerId, NewCategory newCategory){
        log.debug("Requested to create category: [{}]", newCategory);

        Customer customer = getCustomer(customerId);
        Category category;
        if(newCategory.transactionId != null){
            Transaction transaction = this.transactionRepository.findOne(newCategory.transactionId);
            category = new Category(customer,newCategory.name, transaction);
        }else{
            category = new Category(customer,newCategory.name);
        }

        Category created = categoryRepository.save(category);
        return created;
    }

    public Category updateCategory(long customerId, ModifiedCategory modifiedCategory){

        Customer customer = getCustomer(customerId);
        if (isCategoryBelongsToCustomer(modifiedCategory.id, customer)) {

            Category category = this.categoryRepository.findOne(modifiedCategory.id);
            category.name = modifiedCategory.name;
            Category updated = this.categoryRepository.save(category);
            return updated;
        }else{
            throw new RuntimeException(
                    "Security violation: customer id" + customer.id + " tried to access category id " + modifiedCategory.id);
        }
    }

    public void removeCategory(long customerId, long categoryId){

        Customer customer = getCustomer(customerId);
        if (isCategoryBelongsToCustomer(categoryId, customer)) {
            this.categoryRepository.delete(categoryId);
        }
    }

    private Customer getCustomer(Long id) {
        Customer customer = this.customerRepository.findOne(id);
        checkNotNull(customer,"No customer found for id="+id);
        return customer;
    }

    private boolean isCategoryBelongsToCustomer(long id, Customer customer) {
        long count = customer.categories.stream().filter(c -> c.id == id).count();
        if(count == 1){
            return true;
        }
        return false;
    }
}
