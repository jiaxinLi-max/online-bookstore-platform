package com.example.tomatomall.po;

import com.example.tomatomall.vo.QuestionVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime ddl;

    @Column(nullable = false)
    private String answer;

    @Column(columnDefinition = "json", nullable = false)
    private String options; // 实际为 JSON 字符串，如 '["A. 微信", "B. 支付宝", "C. 银行卡"]'

    private String analysis;

    public QuestionVO toVO(Question po) {
        QuestionVO vo = new QuestionVO();
        vo.setId(po.getId());
        vo.setContent(po.getContent());
        vo.setCreateTime(po.getCreateTime());
        vo.setDdl(po.getDdl());
        vo.setAnswer(po.getAnswer());
        vo.setAnalysis(po.getAnalysis());

        // JSON 字符串 → List<String>
        List<String> options = new Gson().fromJson(po.getOptions(), new TypeToken<List<String>>(){}.getType());
        vo.setOptions(options);

        return vo;
    }
}
