package com.example.tomatomall.service;

import com.example.tomatomall.vo.OrderVO;
import com.example.tomatomall.vo.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface OrderService {
    OrderVO sendPay(Integer order_id, HttpServletResponse httpResponse);
    //OrderVO createOrder(Integer userId, List<String> cartItemIds,Object shipping_address,String payment_method);
    OrderVO createOrder(OrderVO orderVO);

    void payNotify(HttpServletRequest request,HttpServletResponse httpServletResponse);

    OrderVO getOrderById(Integer order_id);
}
