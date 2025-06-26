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
        List<Comment> comments = commentRepository.findByProductIdAndParentIsNull(productId);
        return comments.stream()
                .map(Comment::toVO)
                .collect(Collectors.toList());
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
        int sum=0;
        for(Comment comment:comments){
            if(comment.getParent()!=null){
                continue;
            }
            sum++;
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

        update_new_score(comment.getProductId());
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

    /**
     *获取指定评论的所有回复（子评论）
     *
     * 本方法通过给定的父评论ID，从数据库中查询出所有以该评论为父评论的子评论（即回复），
     * 然后将这些评论实体对象（Comment）转换为用于前端展示的视图对象（CommentVO）列表返回
     *
     * 变更前的需求：评论是“单层结构”，仅支持商品的评论（顶层评论），无嵌套
     * 变更后的需求：评论支持“层级结构”，允许用户对某条评论进行回复（即“子评论”或“嵌套评论”）
     *
     * @param commentId 父评论id
     * @return CommentVO 列表，每个元素代表一条回复的视图信息
     */
    @Override
    public List<CommentVO>getReplies(Integer commentId){
        //根据commentId获取该评论的回复
        List<Comment> replies = commentRepository.findByParent_Id(commentId);

        // 使用 Java Stream 对查询结果进行转换：
        // 将每个 Comment 实体对象映射为 CommentVO 视图对象，
        // 最终收集为 List 返回
        return replies.stream()
                .map(Comment::toVO)
                .collect(Collectors.toList());
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
