package com.example.tomatomall.vo;


import com.example.tomatomall.po.Columns;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ColumnsVO {
    private Integer id;
    private String theme;
    private String description;
    private List<String> covers;

    public Columns toPO(){
        Columns columns=new Columns();
        columns.setId(this.id);
        columns.setTheme(this.theme);
        columns.setCovers(this.covers);
        columns.setDescription(this.description);
        return columns;
    }
}
