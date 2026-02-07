package com.example.tomatomall.repository;

import com.example.tomatomall.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByProductId(Integer productId);

    List<Comment>findAllByOrderByTimeDesc();

    List<Comment>findAllByOrderByLikesDesc();

    List<Comment>findAllByProductIdOrderByTimeDesc(Integer productId);

    List<Comment>findAllByProductIdOrderByLikesDesc(Integer productId);

    //使用 Spring Data JPA 提供的命名查询方法，通过 parent_id 字段检索所有对应的子评论。
    //查询语句将自动翻译为如下 SQL：SELECT * FROM comment WHERE parent_id = :parentId;
    List<Comment> findByParent_Id(Integer parentId);

    List<Comment> findByProductIdAndParentIsNull(Integer productId);
}
