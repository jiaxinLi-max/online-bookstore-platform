package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseVO {
    private List<CartVO> items; // 购物车商品列表
    private int total; // 购物车商品种类数量
    private BigDecimal totalAmount; // 购物车总价
}
