package com.example.tomatomall.service;

import com.example.tomatomall.po.Cart;
import com.example.tomatomall.vo.CartResponseVO;
import com.example.tomatomall.vo.CartVO;


import java.util.List;

public interface CartService {

    //添加商品到购物车
    CartVO addPIntoCart(Integer userId, Integer productId, Integer quantity);

    //删除购物车中的商品
    Boolean deletePInCart(Integer userId,Integer cart_item_id);

    //修改购物车中的商品数量
    Boolean modifyQuantity(Integer userId,Integer cart_item_id,Integer quantity);

    //获取购物车商品列表
    CartResponseVO getPInCart(Integer userId);

    Cart findById(Integer id);
}
