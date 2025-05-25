package com.example.tomatomall.repository;

import com.example.tomatomall.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByTitle(String title);
    List<Product>findAllByOrderByRateDesc();
    List<Product> findByTitleContainingIgnoreCase(String title);
}
