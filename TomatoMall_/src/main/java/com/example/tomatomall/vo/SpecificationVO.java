package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationVO {
    private String id;       // 规格项ID
    private String item;     // 规格项名称（如“作者”）
    private String value;    // 规格项内容（如“Robert C. Martin”）
    private String productId; // 关联的商品ID
}
