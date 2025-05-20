package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.repository.CommentRepository;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.service.RankService;
import com.example.tomatomall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public List<ProductVO>getRankInfo(){
        //按照书籍的评分排名
        List<Product>productList=productRepository.findAllByOrderByRateDesc();
        return productList.stream().map(Product::toVO).collect(Collectors.toList());

    }
}
