package com.example.tomatomall.vo;

import com.alibaba.fastjson.JSON;
import com.example.tomatomall.po.Question;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class QuestionVO {
    private Integer id;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime ddl;
    private String answer;
    private List<String> options; // 实际前端/后端传递是 List 格式
    private String analysis;

    public Question toPO() {
        Question po = new Question();
        po.setId(this.getId());
        po.setContent(this.getContent());
        po.setCreateTime(this.getCreateTime());
        po.setDdl(this.getDdl());
        po.setAnswer(this.getAnswer());
        po.setAnalysis(this.getAnalysis());

        // List<String> → JSON 字符串
        String optionsJson = new Gson().toJson(this.getOptions());
        po.setOptions(optionsJson);

        return po;
    }
}
