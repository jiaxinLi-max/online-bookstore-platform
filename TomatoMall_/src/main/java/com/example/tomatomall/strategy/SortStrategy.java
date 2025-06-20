package com.example.tomatomall.strategy;

import java.util.List;

public interface SortStrategy {

    //按时间排序
    List<Object> sortByTime(Integer productId);
    //按点赞数排序
    List<Object> sortByLike(Integer productId);
}
