package com.example.tomatomall.vo;


import com.example.tomatomall.po.Cart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class CartVO {
    private Integer id;//购物车记录
    private Integer userId;//关联用户id
    private Integer productId;//关联商品id
    private Integer quantity;//商品数量
    public Cart toPO(){
        Cart cart=new Cart();
        cart.setId(this.id);
        cart.setUserId(this.userId);
        cart.setProductId(this.productId);
        cart.setQuantity(this.quantity);
        return cart;
    }
}
