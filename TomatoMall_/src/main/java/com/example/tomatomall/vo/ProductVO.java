package com.example.tomatomall.vo;

import com.example.tomatomall.po.Specification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {
    private String id;          // 商品ID
    private String title;       // 商品名称
    private BigDecimal price;   // 商品价格
    private Float rate;        // 评分
    private String description; // 商品描述
    private String cover;       // 商品封面URL
    private String detail;      // 商品详情
    private Set<Specification> specifications; // 商品规格信息


}
