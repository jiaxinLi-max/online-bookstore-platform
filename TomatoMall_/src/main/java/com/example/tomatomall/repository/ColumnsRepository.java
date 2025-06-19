package com.example.tomatomall.repository;

import com.example.tomatomall.po.Columns;
import com.example.tomatomall.po.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColumnsRepository extends JpaRepository<Columns,Integer> {
    Columns findByTheme(String theme);

    @Query("SELECT p FROM Product p JOIN p.columns c WHERE c.id = :columnId")
    List<Product> findProductsByColumnId(@Param("columnId") Integer columnId);

}
