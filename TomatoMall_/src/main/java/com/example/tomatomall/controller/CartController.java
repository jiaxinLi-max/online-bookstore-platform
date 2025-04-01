package com.example.tomatomall.controller;


import com.example.tomatomall.service.CartService;
import com.example.tomatomall.vo.CartVO;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;
    /**
     * 把商品加到购物车
     */
    @PostMapping
    Response<Boolean>addPIntoCart(@PathVariable Integer userId,@PathVariable Integer productId,@PathVariable Integer quantity){
        return Response.buildSuccess(cartService.addPIntoCart(userId,productId,quantity));
    }

    /**
     *删除购物车商品
     */
    @DeleteMapping("/{cart_item_id}")
    Response<Boolean>deletePInCart(@PathVariable Integer userId,@PathVariable Integer cart_item_id){
        return Response.buildSuccess(cartService.deletePInCart(userId,cart_item_id));
    }

    /**
     * 修改购物车商品数量
     */
    @PatchMapping("/{cart_item_id}/quantity")
    Response<Boolean>modifyQuantity(@PathVariable Integer userId,@PathVariable Integer cart_item_id,@PathVariable Integer quantity){
        return Response.buildSuccess(cartService.modifyQuantity(userId,cart_item_id,quantity));
    }

    /**
     * 获取购物车商品列表
     */
    @GetMapping("/")
    Response<List<CartVO>>getPInCart(@PathVariable Integer userId){
        return Response.buildSuccess(cartService.getPInCart(userId));
    }
}
