package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.repository.QuestionRepository;
import com.example.tomatomall.repository.QuestionUsersRelationRepository;
import com.example.tomatomall.service.QuestionService;
import com.example.tomatomall.vo.QuestionUserVO;
import com.example.tomatomall.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServicelmpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionUsersRelationRepository questionUsersRelationRepository;

    @Override
    public Boolean createQuestion(QuestionVO questionVO){

    }
    Boolean answer(QuestionUserVO questionUserVO);
    List<QuestionVO> getQuestionList();
    QuestionVO getQuestion(Integer id);
    List<QuestionVO> getQuestionAnswered(Integer id);
    List<QuestionVO> getQuestionUnanswered(Integer id);
}
