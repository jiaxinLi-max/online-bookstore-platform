package com.example.tomatomall.strategy.startegyImpl;

import com.example.tomatomall.po.Post;
import com.example.tomatomall.service.PostService;
import com.example.tomatomall.strategy.SortStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("post")
// 将该类注册为 Spring 容器中的一个 Bean，并指定其名称为 "post"
// 这样 Spring 会将该 Bean 注入到 Map<String, SortStrategy> 中，key 为 "post"
public class PostSortStrategy implements SortStrategy {

    @Autowired
    private PostService postService;

    /**
     * 按发布时间对所有帖子进行降序排序。
     *
     * @param productId 参数在帖子排序中不使用（为了统一接口结构而保留）
     * @return 排序后的帖子视图对象列表（List<Object> 实际为 List<PostVO>）
     */
    @Override
    public List<Object> sortByTime(Integer productId) {
        return postService.findAllByOrderByTimeDesc()
                .stream().map(Post::toVO).collect(Collectors.toList());
    }

    /**
     * 按点赞数对所有帖子进行降序排序。
     *
     * @param productId 参数在帖子排序中不使用（为了统一接口结构而保留）
     * @return 按点赞数排序后的帖子视图对象列表
     */
    @Override
    public List<Object> sortByLike(Integer productId) {
        return postService.findAllByOrderByLikeDesc()
                .stream().map(Post::toVO).collect(Collectors.toList());
    }
}
