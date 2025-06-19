package com.example.tomatomall.repository;


import com.example.tomatomall.po.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    // 查询所有问题中，ID 在已回答列表中的
    List<Question> findByIdIn(List<Integer> ids);

    // 查询所有问题中，ID 不在已回答列表中的
    List<Question> findByIdNotIn(List<Integer> ids);
}
