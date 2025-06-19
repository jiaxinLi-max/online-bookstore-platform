package com.example.tomatomall.po;


import com.example.tomatomall.vo.ColumnsVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Columns {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="id",nullable = false)
    private Integer id;

    @Basic
    @Column(name="theme",nullable = false)
    private String theme;

    @Basic
    @Column(name="description",nullable = false)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "columns_covers", joinColumns = @JoinColumn(name = "columns_id"))
    @Column(name = "cover_url")
    private List<String> covers = new ArrayList<>();

    @ManyToMany(mappedBy = "columns")
    private Set<Product> products = new HashSet<>();


    public ColumnsVO toVO(){
        ColumnsVO columnsVO=new ColumnsVO();
        columnsVO.setId(this.id);
        columnsVO.setTheme(this.theme);
        columnsVO.setDescription(this.description);
        columnsVO.setCovers(this.covers);
        return columnsVO;
    }
}
