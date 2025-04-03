package com.example.tomatomall.vo;


import com.example.tomatomall.po.Cart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class CartVO {
    private Integer cartItemId;//购物车记录
    private Integer userId;//关联用户id
    private Integer productId;//关联商品id
    private String title;//书名
    private BigDecimal price;
    private String description;
    private String cover;
    private String detail;
    private Integer quantity;//商品数量

    public Cart toPO(){
        Cart cart=new Cart();
        cart.setCartItemId(this.cartItemId);
        cart.setUserId(this.userId);
        cart.setProductId(this.productId);
        cart.setQuantity(this.quantity);
        return cart;
    }
}
