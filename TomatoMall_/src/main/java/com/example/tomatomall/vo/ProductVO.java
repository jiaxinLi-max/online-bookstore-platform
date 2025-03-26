package com.example.tomatomall.vo;

import com.example.tomatomall.po.Product;
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
    private Integer id;          // 商品ID
    private String title;       // 商品名称
    private BigDecimal price;   // 商品价格
    private Float rate;        // 评分
    private String description; // 商品描述
    private String cover;       // 商品封面URL
    private String detail;      // 商品详情
    private Set<Specification> specifications; // 商品规格信息

    public Product toPO(){
        Product product=new Product();
        product.setId(id);
        product.setPrice(this.price);
        product.setRate(this.rate);
        product.setDetail(this.detail);
        product.setSpecifications(this.specifications);
        product.setTitle(this.title);
        product.setCover(this.cover);
        product.setDescription(this.description);
        return product;
    }

}
