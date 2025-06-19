package com.example.tomatomall.repository;

import com.example.tomatomall.po.Question;
import com.example.tomatomall.po.QuestionUsersRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface QuestionUsersRelationRepository extends JpaRepository<QuestionUsersRelation,Integer> {
    Boolean existsByUserIdAndQuestionId(Integer userId,Integer questionId);
    List<QuestionUsersRelation> findAllByUserId(Integer userId);
}
