package com.example.tomatomall.controller;


import com.example.tomatomall.service.CartService;
import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;
    /**
     * 把商品加到购物车
     */
    @PostMapping
    Response<CartVO>addPIntoCart(@RequestParam Integer userId,@RequestParam Integer productId,@RequestParam Integer quantity){
        return Response.buildSuccess(cartService.addPIntoCart(userId,productId,quantity));
    }

    /**
     *删除购物车商品
     */
    @DeleteMapping("/{cart_item_id}")
    Response<Boolean>deletePInCart(@RequestParam Integer userId,@PathVariable Integer cart_item_id){
        return Response.buildSuccess(cartService.deletePInCart(userId,cart_item_id));
    }

    /**
     * 修改购物车商品数量
     */
    @PatchMapping("/{cart_item_id}/quantity")
    Response<Boolean>modifyQuantity(@RequestParam Integer userId,@PathVariable Integer cart_item_id,@RequestParam Integer quantity){
        return Response.buildSuccess(cartService.modifyQuantity(userId,cart_item_id,quantity));
    }

    /**
     * 获取购物车商品列表
     */
    @GetMapping("/")
    Response<CartResponseVO>getPInCart(@RequestParam Integer userId){
        return Response.buildSuccess(cartService.getPInCart(userId));
    }

//    @PostMapping("/checkout")
//    Response<OrderVO> createOrder(@RequestParam Integer userId,@RequestParam List<String> cartItemIds,@RequestParam Object shipping_address,@RequestParam String payment_method){
//        return Response.buildSuccess(orderService.createOrder(userId,cartItemIds,shipping_address,payment_method));
//    }

    @PostMapping("/checkout")
    Response<OrderVO> createOrder(@RequestBody OrderVO orderVO){
        return Response.buildSuccess(orderService.createOrder(orderVO));
    }
}
