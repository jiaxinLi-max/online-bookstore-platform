package com.example.tomatomall.repository;

import com.example.tomatomall.po.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecificationRepository extends JpaRepository<Specification,Integer> {
    void deleteByProductId(Integer id);
}
