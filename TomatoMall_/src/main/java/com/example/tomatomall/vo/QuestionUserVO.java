package com.example.tomatomall.vo;

import com.example.tomatomall.po.QuestionUsersRelation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class QuestionUserVO {
    private Integer id;

    private Integer questionId;

    private Integer userId;
    private LocalDateTime answerTime;

    private String answer;
    public QuestionUsersRelation toPO(QuestionUserVO vo) {
        QuestionUsersRelation po = new QuestionUsersRelation();
        po.setId(vo.getId());
        po.setQuestionId(vo.getQuestionId());
        po.setUserId(vo.getUserId());
        po.setAnswerTime(vo.getAnswerTime());
        po.setAnswer(vo.getAnswer());
        return po;
    }
}
