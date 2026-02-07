package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Question;
import com.example.tomatomall.po.QuestionUsersRelation;
import com.example.tomatomall.repository.QuestionRepository;
import com.example.tomatomall.repository.QuestionUsersRelationRepository;
import com.example.tomatomall.service.QuestionService;
import com.example.tomatomall.vo.QuestionUserVO;
import com.example.tomatomall.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServicelmpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionUsersRelationRepository questionUsersRelationRepository;



    @Override
    public Boolean createQuestion(QuestionVO questionVO){
        Question question=questionVO.toPO();
        question.setCreateTime(LocalDateTime.now());
        questionRepository.save(question);
        return true;
    }
    @Override
    public Boolean answer(QuestionUserVO questionUserVO){

        if(questionUsersRelationRepository.existsByUserIdAndQuestionId(questionUserVO.getUserId(),questionUserVO.getQuestionId())){
            throw TomatoMallException.AlreadyAnswer();
        }
        QuestionUsersRelation questionUsersRelation=questionUserVO.toPO();
        questionUsersRelation.setAnswerTime(LocalDateTime.now());
        questionUsersRelationRepository.save(questionUsersRelation);
        return true;
    }
    @Override
    public List<QuestionVO> getQuestionList(){
        return questionRepository.findAll().stream().map(Question::toVO).collect(Collectors.toList());
    }
    @Override
    public QuestionVO getQuestion(Integer id){
        Question question=questionRepository.findById(id).orElse(null);
        if(question==null){
            throw TomatoMallException.questionNotExist();
        }
        return question.toVO();
    }
    @Override
    public List<QuestionVO> getQuestionAnswered(Integer id){
        List<Integer> answeredIds = questionUsersRelationRepository.findAllByUserId(id)
                .stream()
                .map(QuestionUsersRelation::getQuestionId)
                .distinct()
                .collect(Collectors.toList());

        if (answeredIds.isEmpty()) return Collections.emptyList();

        List<Question> questions = questionRepository.findByIdIn(answeredIds);
        return questions.stream().map(Question::toVO).collect(Collectors.toList());
    }
    @Override
    public List<QuestionVO> getQuestionUnanswered(Integer id){
        List<Integer> answeredIds = questionUsersRelationRepository.findAllByUserId(id)
                .stream()
                .map(QuestionUsersRelation::getQuestionId)
                .distinct()
                .collect(Collectors.toList());

        if (answeredIds.isEmpty()) return Collections.emptyList();

        List<Question> questions = questionRepository.findByIdNotIn(answeredIds);
        return questions.stream().map(Question::toVO).collect(Collectors.toList());
    }

    @Override
    public String getAnswer(Integer userId,Integer questionId){
        QuestionUsersRelation questionUsersRelation=questionUsersRelationRepository.findByUserIdAndQuestionId(userId,questionId);
        return questionUsersRelation.getAnswer();
    }
}
