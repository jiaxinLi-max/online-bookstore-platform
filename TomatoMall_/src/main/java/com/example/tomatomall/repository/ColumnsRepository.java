package com.example.tomatomall.repository;

import com.example.tomatomall.po.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnsRepository extends JpaRepository<Columns,Integer> {
    Columns findByTheme(String theme);
}
