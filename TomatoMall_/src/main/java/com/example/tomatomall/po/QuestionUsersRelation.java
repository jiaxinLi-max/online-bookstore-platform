package com.example.tomatomall.po;

import com.example.tomatomall.vo.QuestionUserVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_question")
public class QuestionUsersRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "answer_time", nullable = false)
    private LocalDateTime answerTime;

    @Column(name = "answer", nullable = false)
    private String answer;

    public QuestionUserVO toVO(QuestionUsersRelation po) {
        QuestionUserVO vo = new QuestionUserVO();
        vo.setId(po.getId());
        vo.setQuestionId(po.getQuestionId());
        vo.setUserId(po.getUserId());
        vo.setAnswerTime(po.getAnswerTime());
        vo.setAnswer(po.getAnswer());
        return vo;
    }
}
