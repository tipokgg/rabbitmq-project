package com.geekbrains.rabbitmqjsonproducts.repository;

import com.geekbrains.rabbitmqjsonproducts.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findAllByPriceBetween(Pageable pageable, double min, double max);
    Product findOneByTitle(String title);
}
