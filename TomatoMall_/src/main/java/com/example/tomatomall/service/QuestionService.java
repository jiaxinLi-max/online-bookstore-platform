package com.example.tomatomall.service;

import com.example.tomatomall.vo.QuestionUserVO;
import com.example.tomatomall.vo.QuestionVO;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public interface QuestionService {
    Boolean createQuestion(QuestionVO questionVO);
    Boolean answer(QuestionUserVO questionUserVO);
    List<QuestionVO> getQuestionList();
    QuestionVO getQuestion(Integer id);
    List<QuestionVO> getQuestionAnswered(Integer id);
    List<QuestionVO> getQuestionUnanswered(Integer id);
}
