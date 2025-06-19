package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Comment;


import com.example.tomatomall.po.Product;
import com.example.tomatomall.repository.CommentRepository;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class CommentServicelmpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProductService productService;

    @Override
    public List<CommentVO> getAllCommentInfo(Integer productId){
        List<Comment> comments = commentRepository.findByProductId(productId);
        // 假设你有一个工具方法或构造器能把 Comment 转成 CommentVO
        return comments.stream().map(Comment::toVO).collect(Collectors.toList());
    }
    @Override
    public CommentVO getCommentInfo(Integer id){
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment == null) {
                throw TomatoMallException.commentNotExist();
            }
            return comment.toVO();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format: " + id);  // 打印错误的 ID 格式
            throw TomatoMallException.invalidProductId();
        }
    }
    @Override
    public String updateCommentInfo(CommentVO commentVO){
        Comment comment = commentRepository.findById(commentVO.getId()).orElse(null);

        if (comment == null) {
            throw TomatoMallException.commentNotExist();
        }
        if(commentVO.getContent()!=null){
            comment.setContent(commentVO.getContent());
        }
        if(commentVO.getScore()!=null){
            comment.setScore(commentVO.getScore());
        }
        comment.setTime(LocalDateTime.now());
        commentRepository.save(comment);
        update_new_score(comment.getProductId());
        return "更新成功";
    }

    private void update_new_score(Integer productId){
        List<Comment> comments = commentRepository.findByProductId(productId);
        Product product=productService.findById(productId);
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        float sum_score=0;
        int sum=comments.size();
        for(Comment comment:comments){
            sum_score+=comment.getScore();
        }
        sum_score=sum_score*2;
        float new_rate=sum_score/sum;
        product.setRate(new_rate);
        productService.saveProduct(product);
    }

    @Override
    public String addComment(CommentVO commentVO){
        Comment parent = null;
        if (commentVO.getParentId() != null) {
            parent = commentRepository.findById(commentVO.getParentId()).orElse(null);
            if (parent == null) {
                throw TomatoMallException.parentCommentNotExist();
            }
        }
        Comment comment=commentVO.toPO(parent);
        comment.setLikes(0);
        comment.setTime(LocalDateTime.now());
        commentRepository.save(comment);
        update_new_score(comment.getProductId());
        return "创建成功";
    }
    @Override
    public String deleteComment(Integer id){
        Comment comment=commentRepository.findById(id).orElse(null);

        if (comment == null) {
            throw TomatoMallException.commentNotExist();
        }


        commentRepository.deleteById(id);
        return "删除成功";
    }
    @Override
    public String likeComment(Integer id,Integer userId){
        Comment comment=commentRepository.findById(id).orElse(null);
        if(comment==null){
            throw TomatoMallException.commentNotExist();
        }
        if (comment.getLikedUserIds().contains(userId)) {
            comment.setLikes(comment.getLikes()-1);
            comment.getLikedUserIds().remove(userId);
            commentRepository.save(comment);
            return "取消点赞";
        }else {
            comment.setLikes(comment.getLikes()+1);
            comment.getLikedUserIds().add(userId);
            commentRepository.save(comment);
            return "点赞成功";
        }

    }

    @Override
    public List<Comment>findAllByOrderByTimeDesc(){
        return commentRepository.findAllByOrderByTimeDesc();
    }
    @Override
    public List<Comment>findAllByOrderByLikesDesc(){
        return commentRepository.findAllByOrderByLikesDesc();
    }
    @Override
    public List<Comment>findAllByProductIdOrderByTimeDesc(Integer productId){
        return commentRepository.findAllByProductIdOrderByTimeDesc(productId);
    }

    @Override
    public  List<Comment>findAllByProductIdOrderByLikesDesc(Integer productId){
        return commentRepository.findAllByProductIdOrderByLikesDesc(productId);
    }
}
