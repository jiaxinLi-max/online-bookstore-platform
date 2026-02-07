package com.example.tomatomall.service;

import java.util.List;

public interface SortService {
    List<Object> sortByTime(String type,Integer productId);

    List<Object> sortByLike(String type, Integer productId);
}
