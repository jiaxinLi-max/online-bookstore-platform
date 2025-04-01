package com.example.tomatomall.service;

import com.example.tomatomall.vo.OrderVO;

public interface OrderService {
    OrderVO sendPay(Integer order_id);
}
