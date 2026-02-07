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
    public QuestionUsersRelation toPO() {
        QuestionUsersRelation po = new QuestionUsersRelation();
        po.setId(this.getId());
        po.setQuestionId(this.getQuestionId());
        po.setUserId(this.getUserId());
        po.setAnswerTime(this.getAnswerTime());
        po.setAnswer(this.getAnswer());
        return po;
    }
}
