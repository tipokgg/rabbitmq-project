package com.geekbrains.rabbitmqjsonproducts.repository;

import com.geekbrains.rabbitmqjsonproducts.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
