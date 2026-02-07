package com.example.tomatomall.service.serviceImpl;


import com.example.tomatomall.po.Product;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.service.RankService;
import com.example.tomatomall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;


    @Override
    public List<ProductVO>getRankInfo(){
        //按照书籍的评分排名
        List<Product>productList=productService.findAllByOrderByRateDesc();
        return productList.stream().map(Product::toVO).collect(Collectors.toList());

    }
}
