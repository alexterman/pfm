package com.noname.digital.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.noname.digital.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

}