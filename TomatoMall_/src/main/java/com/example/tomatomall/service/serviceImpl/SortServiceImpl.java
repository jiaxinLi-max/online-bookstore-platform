package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.strategy.SortStrategyContext;
import com.example.tomatomall.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SortServiceImpl implements SortService {

    //策略上下文类
    @Autowired
    private SortStrategyContext strategyContext;

    @Override
    public List<Object> sortByTime(String type, Integer productId) {
        return strategyContext.getStrategy(type).sortByTime(productId);
    }

    @Override
    public List<Object> sortByLike(String type, Integer productId) {
        return strategyContext.getStrategy(type).sortByLike(productId);
    }
}
