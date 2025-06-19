package com.example.tomatomall.controller;

import com.example.tomatomall.service.QuestionService;
import com.example.tomatomall.vo.QuestionUserVO;
import com.example.tomatomall.vo.QuestionVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @PostMapping("/create")
    public Response<Boolean> createQuestion(@RequestBody QuestionVO questionVO) {
        return Response.buildSuccess(questionService.createQuestion(questionVO));
    }

    @PostMapping("/question")
    public Response<Boolean> answer(@RequestBody QuestionUserVO questionUserVO) {
        return Response.buildSuccess(questionService.answer(questionUserVO));
    }
    @GetMapping("/list")
    public Response<List<QuestionVO>> getQuestionList(){
        return Response.buildSuccess(questionService.getQuestionList());
    }
    @GetMapping("/detail/{questionId}")
    public Response<QuestionVO> getQuestion(@PathVariable(value="questionId")Integer questionId){
        return Response.buildSuccess(questionService.getQuestion(questionId));
    }
    @GetMapping("/answered/{userId}")
    public Response<List<QuestionVO>> getListAnswered(@PathVariable(value="userId")Integer userId){
        return  Response.buildSuccess(questionService.getQuestionAnswered(userId));
    }
    @GetMapping("/unanswered/{userId}")
    public Response<List<QuestionVO>> getListUnanswered(@PathVariable(value="userId")Integer userId){
        return  Response.buildSuccess(questionService.getQuestionUnanswered(userId));
    }
}
