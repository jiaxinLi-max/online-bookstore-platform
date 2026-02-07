package com.example.tomatomall.vo;

import com.example.tomatomall.po.Columns;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {
    private Integer id;          // 商品ID
    private String title;       // 商品名称
    private BigDecimal price;   // 商品价格
    private Float rate;        // 评分
    private String description; // 商品描述
    private List<String> cover;       // 商品封面URL
    private String detail;      // 商品详情
    private Set<Specification> specifications = new HashSet<>();; // 商品规格信息
    private List<Integer> columnIds;

    public Product toPO(Set<Columns> columns) {
        Product product = new Product();
        product.setId(this.id);
        product.setPrice(this.price);
        product.setRate(this.rate);
        product.setDetail(this.detail);
        product.setSpecifications(this.specifications);
        product.setTitle(this.title);
        product.setCover(this.cover);
        product.setDescription(this.description);
        product.setColumns(columns);
        return product;
    }

}
