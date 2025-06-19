package com.example.tomatomall.vo;

import com.alibaba.fastjson.JSON;
import com.example.tomatomall.po.Question;
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

    public Question toPO(QuestionVO vo) {
        Question po = new Question();
        po.setId(vo.getId());
        po.setContent(vo.getContent());
        po.setCreateTime(vo.getCreateTime());
        po.setDdl(vo.getDdl());
        po.setAnswer(vo.getAnswer());
        po.setAnalysis(vo.getAnalysis());

        // List<String> → JSON 字符串
        String optionsJson = new Gson().toJson(vo.getOptions());
        po.setOptions(optionsJson);

        return po;
    }
}
