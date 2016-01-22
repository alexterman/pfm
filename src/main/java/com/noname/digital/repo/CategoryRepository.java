package com.noname.digital.repo;

import com.noname.digital.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alex on 1/15/16.
 */
public interface CategoryRepository extends CrudRepository<Category,Long> {

}
