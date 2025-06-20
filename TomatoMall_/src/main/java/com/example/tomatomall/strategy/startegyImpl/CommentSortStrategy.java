package com.example.tomatomall.strategy.startegyImpl;

import com.example.tomatomall.po.Comment;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.strategy.SortStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("comment")// 将当前类注册为 Spring 的 Bean，并命名为 "comment"
// 这样可以通过 Map<String, SortStrategy> 注入时以 "comment" 为 key 自动识别

public class CommentSortStrategy implements SortStrategy {

    @Autowired
    private CommentService commentService;

    /**
     * 按时间降序排序评论
     *
     * @param productId 商品 ID（可选）。如果为 null，则返回所有评论；否则返回该商品的评论
     * @return List<Object>：将 Comment 转为 CommentVO 后的结果列表（用于前端展示）
     */
    @Override
    public List<Object> sortByTime(Integer productId) {
        List<Comment> comments = (productId == null) ?
                commentService.findAllByOrderByTimeDesc() :
                commentService.findAllByProductIdOrderByTimeDesc(productId);
        return comments.stream().map(Comment::toVO).collect(Collectors.toList());
    }

    /**
     * 按点赞数降序排序评论
     *
     * @param productId 商品 ID（可选）。如果为 null，则返回所有评论；否则返回该商品的评论
     * @return List<Object>：转换为 CommentVO 的结果列表
     */
    @Override
    public List<Object> sortByLike(Integer productId) {
        List<Comment> comments = (productId == null) ?
                commentService.findAllByOrderByLikesDesc() :
                commentService.findAllByProductIdOrderByLikesDesc(productId);
        return comments.stream().map(Comment::toVO).collect(Collectors.toList());
    }
}
