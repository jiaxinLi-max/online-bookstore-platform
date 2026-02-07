package com.example.tomatomall.po;

import com.example.tomatomall.vo.SpecificationVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Specification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;

    @Basic
    @Column(name = "item",nullable = false)
    private String item;  // 规格名称（如 颜色/尺寸）

    @Basic
    @Column(name = "value",nullable = false)
    private String value; // 规格值（如 红色/XL）

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore  // 关键：防止 product 进入无限递归
    private Product product;

    public SpecificationVO toVO(){
        SpecificationVO specificationVO=new SpecificationVO();
        specificationVO.setId(this.id);
        specificationVO.setItem(this.item);
        specificationVO.setValue(this.value);
        if (this.product != null) {
            specificationVO.setProductId(this.product.getId());
        } else {
            specificationVO.setProductId(null);
        }

        return specificationVO;
    }

}
