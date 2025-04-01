package com.example.tomatomall.po;

import com.example.tomatomall.vo.CartVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Cart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="id",nullable = false)
    private Integer id;//购物车记录

    @Basic
    @Column(name="user_id",nullable = false)
    private Integer userId;//关联用户id

    @Basic
    @Column(name="product_id",nullable = false)
    private Integer productId;//关联商品id

    @Basic
    @Column(name="quantity",nullable = false)
    private Integer quantity;//商品数量

    public CartVO toVO(){
        CartVO cartVO=new CartVO();
        cartVO.setId(this.id);
        cartVO.setProductId(this.productId);
        cartVO.setUserId(this.userId);
        cartVO.setQuantity(this.quantity);
        return cartVO;
    }

}
