package com.example.tomatomall.controller;


import com.alipay.api.internal.util.AlipaySignature;
import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.vo.AccountVO;
import com.example.tomatomall.vo.OrderVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;


    @PostMapping("/{order_id}/pay")
    public Response<OrderVO> getUser(@PathVariable(value="order_id")Integer order_id, HttpServletResponse httpResponse) {
        return Response.buildSuccess(orderService.sendPay(order_id,httpResponse));
    }

//    @PostMapping("/{order_id}/pay")
//    public Response<OrderVO> getUser(@PathVariable(value="order_id")Integer order_id) {
//        return Response.buildSuccess(orderService.sendPay(order_id));
//    }
    @PostMapping("/notify")
    public void payNotify(HttpServletRequest request,HttpServletResponse response){
        Response.buildSuccess(orderService.payNotify(request,response));
    }

}
