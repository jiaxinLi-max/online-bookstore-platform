package com.example.tomatomall.controller;


import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.OrderVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 获取用户详情
     */
    @PostMapping("/{order_id}/pay")
    public Response<OrderVO> getUser(@PathVariable(value="order_id")Integer order_id) {
        return Response.buildSuccess(orderService.sendPay(order_id));
    }
}
