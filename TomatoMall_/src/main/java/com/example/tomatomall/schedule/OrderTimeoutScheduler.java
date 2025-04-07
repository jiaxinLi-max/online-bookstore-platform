package com.example.tomatomall.schedule;

import com.example.tomatomall.po.Cart;
import com.example.tomatomall.po.Order;
import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.repository.CartRepository;

import com.example.tomatomall.repository.OrderRepository;
import com.example.tomatomall.repository.StockpileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Component
public class OrderTimeoutScheduler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StockpileRepository stockpileRepository;

    @Transactional
    @Scheduled(fixedRate = 60 * 1000) // 每分钟执行一次
    public void releaseExpiredOrders() {
        Date now = new Date();
        long timeoutMillis = 30 * 60 * 1000L;

        List<Order> pendingOrders = orderRepository.findAllByStatus("PENDING");

        for (Order order : pendingOrders) {
            if (now.getTime() - order.getCreateTime().getTime() >= timeoutMillis) {
                // 超过30分钟未支付，取消订单并释放库存
                order.setStatus("TIMEOUT");
                orderRepository.save(order);

                for (Integer cartItemId : order.getCartItemIds()) {
                    Cart cartItem = cartRepository.findById(cartItemId).orElse(null);
                    if (cartItem != null) {
                        Stockpile stockpile = stockpileRepository.findByProductId(cartItem.getProductId());
                        if (stockpile != null) {
                            stockpile.setAmount(stockpile.getAmount() + cartItem.getQuantity());
                            stockpile.setFrozen(stockpile.getFrozen() - cartItem.getQuantity());
                            stockpileRepository.save(stockpile);
                        }
                    }
                }
            }
        }
    }
}
