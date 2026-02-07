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

@Component // 标记为 Spring 管理的组件，用于注册定时任务
public class OrderTimeoutScheduler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StockpileRepository stockpileRepository;

    /**
     * 定时任务方法：每隔 1 分钟执行一次，用于检查超时未支付的订单。
     * 如果发现订单已超过 30 分钟未支付，则自动将订单状态置为 TIMEOUT，并释放库存。
     */
    @Transactional // 保证整个方法中的数据库操作在一个事务中执行
    @Scheduled(fixedRate = 60 * 1000) // 每分钟执行一次
    public void releaseExpiredOrders() {
        Date now = new Date();
        long timeoutMillis = 30 * 60 * 1000L;

        // 查询所有“待支付”的订单（PENDING 状态）
        List<Order> pendingOrders = orderRepository.findAllByStatus("PENDING");

        for (Order order : pendingOrders) {
            // 判断该订单是否已超时
            if (now.getTime() - order.getCreateTime().getTime() >= timeoutMillis) {
                // 超过30分钟未支付，取消订单并释放库存
                order.setStatus("TIMEOUT");
                orderRepository.save(order);

                // 遍历订单中的每个购物车条目 ID，逐一释放库存
                for (Integer cartItemId : order.getCartItemIds()) {
                    // 获取购物车条目
                    Cart cartItem = cartRepository.findById(cartItemId).orElse(null);

                    if (cartItem != null) {
                        // 获取对应商品的库存记录
                        Stockpile stockpile = stockpileRepository.findByProductId(cartItem.getProductId());

                        if (stockpile != null) {
                            // 将该商品数量归还到“可用库存”，并减少“冻结库存”
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
